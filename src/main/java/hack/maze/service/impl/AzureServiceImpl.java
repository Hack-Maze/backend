package hack.maze.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobCorsRule;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobServiceProperties;
import hack.maze.entity.Maze;
import hack.maze.entity.Type;
import hack.maze.service.AzureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AzureServiceImpl implements AzureService {

    private final RestTemplate restTemplate;

    @Value("${azure.github.token}")
        private String githubToken;

    private final BlobServiceClient blobServiceClient;
    private final List<String> allowedContentTypesForImages = List.of("image/jpeg", "image/png", "image/gif");

    @Override
    public String sendImageToAzure(MultipartFile image, String containerBlobName, Long blobName) throws IOException {
        if (!checkImage(image)) {
            return "";
        }

        //! make sure the file type is dockerfile or not .....!!!


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
    public String sendImageToAzure(MultipartFile file, String containerBlobName, Long blobName, Type type) throws IOException {
        BlobContainerClient imagesContainer = createBlobContainerIfNotExist(containerBlobName);
        setCorsRules();
        if (type.equals(Type.DOWNLOADABLE_FILE)) {
            log.info("trying to upload DOWNLOADABLE_FILE.....");
            String fileName = String.format("%s/%s", blobName.toString(), file.getOriginalFilename());
            BlobClient blobClient = imagesContainer.getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl();
        } else {
            if (!checkArchive(file)) {
                log.warn("file not uploaded");
                throw new IOException("file type is not supported");
            }
            log.info("trying to upload DOCKER_FILE.....");
            String compressedFile = String.format("%s/%s", blobName, file.getOriginalFilename());
            log.info("compressedFile: {}\n", compressedFile);
            BlobClient blobClient = imagesContainer.getBlobClient(compressedFile);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            return blobClient.getBlobUrl();
        }
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
    public String runImageBuildWorkFlow(Maze maze) {
        String url = "https://api.github.com/repos/Hack-Maze/backend-ziadamer/actions/workflows/ACI.yml/dispatches";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        headers.set(HttpHeaders.AUTHORIZATION, "token " + githubToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info(headers.toString());

        HttpEntity<Map<String, Object>> entity = getMapHttpEntity(headers, maze.getFile(), maze.getTitle());

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return "ttl.sh/" + maze.getTitle() + ":24h";
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
