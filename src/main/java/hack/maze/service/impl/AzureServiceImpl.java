package hack.maze.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobCorsRule;
import com.azure.storage.blob.models.BlobServiceProperties;
import hack.maze.service.AzureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static hack.maze.constant.AzureConstant.IMAGES_BLOB_CONTAINER_NAME;


@Service
@RequiredArgsConstructor
@Slf4j
public class AzureServiceImpl implements AzureService {

    private final BlobServiceClient blobServiceClient;
    private final List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "image/gif");

    @Override
    public String sendImageToAzure(MultipartFile image) throws IOException {
        checkImage(image);

        // get blob container and create it if not exist
        BlobContainerClient imagesContainer = createBlobContainerIfNotExist(IMAGES_BLOB_CONTAINER_NAME);

        // set rules
        setCorsRules();

        // upload image
        BlobClient blobClient = imagesContainer.getBlobClient(image.getOriginalFilename());
        blobClient.upload(image.getInputStream(), image.getSize(), true);

        return blobClient.getBlobUrl();
    }

    private void checkImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!allowedContentTypes.contains(image.getContentType())) {
            throw new IllegalArgumentException("Invalid image type: " + image.getContentType());
        }
        if (image.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new IllegalArgumentException("File size exceeds the limit of 10MB");
        }
    }

    @Override
    public BlobContainerClient createBlobContainerIfNotExist(String containerBlobName) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerBlobName);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }
        return blobContainerClient;
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
