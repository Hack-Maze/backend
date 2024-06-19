package hack.maze.service.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobCorsRule;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobServiceProperties;
import hack.maze.dto.DockerfileInfoDTO;
import hack.maze.entity.AzureContainer;
import hack.maze.entity.Maze;
import hack.maze.entity.Type;
import hack.maze.service.AzureContainerService;
import hack.maze.service.AzureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
@RequiredArgsConstructor
@Slf4j
public class AzureServiceImpl implements AzureService {

    private final RestTemplate restTemplate;
    private final AzureContainerService azureContainerService;

    @Value("${azure.github.token}")
    private String githubToken;

    private final BlobServiceClient blobServiceClient;
    private final List<String> allowedContentTypesForImages = List.of("image/jpeg", "image/png", "image/gif");

    @Override
    public String sendImageToAzure(MultipartFile image, String containerBlobName, Long blobName) throws IOException {
        if (!checkImage(image)) {
            return "";
        }

        // get blob container and create it if not exist
        BlobContainerClient imagesContainer = createBlobContainerIfNotExist(containerBlobName);

        // extract image extension
        Objects.requireNonNull(image.getOriginalFilename(), "Image name could not be null");
        String imageExtension = image.getOriginalFilename().split("\\.")[image.getOriginalFilename().split("\\.").length - 1];

        // construct image name
        // blobName + "/" + blobName.toString() + "." + imageExtension
        String imageName = String.format("%s/%s.%s", blobName.toString(), blobName, imageExtension);

        // set rules
        setCorsRules();

        // upload image
        BlobClient blobClient = imagesContainer.getBlobClient(imageName);
        blobClient.upload(image.getInputStream(), image.getSize(), true);

        return blobClient.getBlobUrl();
    }

    @Override
    @Transactional
    public String sendImageToAzure(MultipartFile file, String containerBlobName, Maze maze, Type type) throws IOException {
        BlobContainerClient imagesContainer = createBlobContainerIfNotExist(containerBlobName);
        setCorsRules();
        if (type.equals(Type.DOWNLOADABLE_FILE)) {
            log.info("trying to upload DOWNLOADABLE_FILE.....");
            String fileName = String.format("%s/%s", maze.getId().toString(), file.getOriginalFilename());
            BlobClient blobClient = imagesContainer.getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl();
        } else {
            if (!checkArchive(file)) {
                log.warn("file not uploaded");
                throw new IOException("file type is not supported");
            }
            log.info("trying to upload DOCKER_FILE.....");
            String compressedFile = String.format("%s/%s", maze.getId(), file.getOriginalFilename());
            log.info("compressedFile: {}\n", compressedFile);
            BlobClient blobClient = imagesContainer.getBlobClient(compressedFile);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            log.info("after uploading to azure blob storage");
            setEnvTemplateAndPortFromZipFile(maze, file);
            log.info("set env and port to the maze");
            return blobClient.getBlobUrl();
        }
    }

    @Transactional
    protected void setEnvTemplateAndPortFromZipFile(Maze maze, MultipartFile file) throws IOException {
        File dockerfilePath = getDockerfileFromZipFile(file);
        if (dockerfilePath == null) {
            throw new FileNotFoundException("dockerfile can't be found");
        }
        DockerfileInfoDTO dockerfileInfoDTO = loopThroughDockerFileAndGetEnvAndPorts(dockerfilePath);
        maze.setEnvTemplate(dockerfileInfoDTO.env());
        maze.setPorts(dockerfileInfoDTO.ports());
    }

    private DockerfileInfoDTO loopThroughDockerFileAndGetEnvAndPorts(File dockerfile) {
        Map<String, String> env = new HashMap<>();
        List<Integer> ports = new ArrayList<>();
        BufferedReader reader;

        try {

            reader = new BufferedReader(new FileReader(dockerfile));
            String line = reader.readLine();

            while (line != null) {
                if (line.contains("ENV") || line.contains("env")) {
                    String[] split = line.split("\\s+");
                    if (!split[2].contains("FLAG_PLACEHOLDER")) {
                        throw new IOException("FLAG_PLACEHOLDER should be specified");
                    }
                    env.put(split[1], split[2]);
                } else if (line.contains("EXPOSE") || line.contains("expose")) {
                    String[] split = line.split("\\s+");
                    for (int i = 1; i < split.length; i++) {
                        if (split[i].contains("-")) {
                            String[] split2 = split[i].split("-");
                            for (int j = Integer.parseInt(split2[0]); j <= Integer.parseInt(split2[1]); j++) {
                                ports.add(j);
                            }
                            continue;
                        }
                        ports.add(Integer.parseInt(split[i]));
                    }
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return DockerfileInfoDTO.builder().env(env).ports(ports).build();
    }


    private File getDockerfileFromZipFile(MultipartFile zippedFile) throws IOException {
        File dir = unzipFile(zippedFile);
        File dockerfile = null;
        try {
            dockerfile = getDockerfileFromListOfFiles(dir);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            deleteDirectory(dir);
        }
        return dockerfile;
    }

    private void deleteDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            return;
        }

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }

        directory.delete();
    }

    private File unzipFile(MultipartFile zippedFile) throws IOException {
        File destDir = new File("src/main/java");
        String fileName = Objects.requireNonNull(zippedFile.getOriginalFilename()).split("\\.")[0];
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (ZipInputStream zipInputStream =
                     new ZipInputStream(
                             new FileInputStream(convert(zippedFile)))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File file = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parentDir = file.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }
        return new File("src/main/java/" + fileName);
    }

    public File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

        return file;
    }

    private File getDockerfileFromListOfFiles(File dir) {
        File[] files = Objects.requireNonNull(dir).listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.getName().contains("dockerfile") || file.getName().contains("Dockerfile")) {
                log.info("FOUND DOCKER FILE....");
                return file;
            }
        }
        return null;
    }

    @Override
    public void removeImageFromAzure(String containerBlobName, String prefix) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerBlobName);
        PagedIterable<BlobItem> blobs = blobContainerClient.listBlobsByHierarchy(prefix);
        List<String> blobNames = new ArrayList<>();
        for (BlobItem blobItem : blobs) {
            blobNames.add(blobItem.getName());
        }

        // delete each blob
        for (String blobName : blobNames) {
            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
            blobClient.delete();
        }
    }

    private boolean checkImage(MultipartFile image) {
        if (image.isEmpty()) {
            return false;
        }
        return allowedContentTypesForImages.contains(image.getContentType());
    }

    private boolean checkArchive(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        String fileExtension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[file.getOriginalFilename().split("\\.").length - 1];
        log.info("fileExtension: {}\n", fileExtension);
        return fileExtension.equals("zip");
    }

    @Override
    public BlobContainerClient createBlobContainerIfNotExist(String containerBlobName) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerBlobName);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }
        return blobContainerClient;
    }

    @Override
    @Transactional
    public String runImageBuildWorkFlow(Maze maze) {
        String url = "https://api.github.com/repos/Hack-Maze/backend-ziadamer/actions/workflows/ACI.yml/dispatches";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        headers.set(HttpHeaders.AUTHORIZATION, "token " + githubToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info(headers.toString());

        HttpEntity<Map<String, Object>> entity = getMapHttpEntity(headers, maze.getFile(), maze.getTitle());

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String ret = "ttl.sh/" + maze.getTitle() + ":24h";
        maze.setDockerImageName(ret);
        return ret;
    }

    @Override
    @Transactional
    public String runYourContainer(Maze maze) {
//        String dockerImageName = maze.getDockerImageName();
        createAzureContainerFromImage(maze);
        return "";
    }

    @Transactional
    protected void createAzureContainerFromImage(Maze maze) {
        //! TODO: check if container id already running
        AzureContainer savedAzureContainer = azureContainerService.saveAzureContainer(AzureContainer
                .builder()
                .build());
    }

    private static HttpEntity<Map<String, Object>> getMapHttpEntity(HttpHeaders headers, String filePath, String mazeTitle) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ref", "refs/heads/main");

        Map<String, String> inputs = new HashMap<>();
        filePath = filePath.replace("%2F", "/");
        inputs.put("DockerArchiveUrl", filePath);
        inputs.put("DockerTag", mazeTitle);

        log.info("filePath: {}", filePath);
        log.info("mazeTitle: {}", mazeTitle);

        requestBody.put("inputs", inputs);

        return new HttpEntity<>(requestBody, headers);
    }


    public void setCorsRules() {
        BlobServiceProperties properties = blobServiceClient.getProperties();
        BlobCorsRule corsRule = new BlobCorsRule()
                .setAllowedOrigins("*")  // Allow all origins
                .setAllowedMethods("GET")
                .setAllowedHeaders("*")
                .setExposedHeaders("*")
                .setMaxAgeInSeconds(3600);
        properties.setCors(List.of(corsRule));
        blobServiceClient.setProperties(properties);
    }

}
