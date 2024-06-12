package hack.maze.service;

import com.azure.storage.blob.BlobContainerClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AzureService {
    String sendImageToAzure(MultipartFile image, String containerBlobName, Long blobName) throws IOException;
    void removeImageFromAzure(String containerBlobName, String imagePath);
    BlobContainerClient createBlobContainerIfNotExist(String containerBlobName);
}
