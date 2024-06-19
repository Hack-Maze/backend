package hack.maze.service;

import com.azure.storage.blob.BlobContainerClient;
import hack.maze.entity.Maze;
import hack.maze.entity.Type;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AzureService {
    String sendImageToAzure(MultipartFile image, String containerBlobName, Long blobName) throws IOException;
    String sendImageToAzure(MultipartFile file, String containerBlobName, Maze maze, Type type) throws IOException;
    void removeImageFromAzure(String containerBlobName, String imagePath);
    BlobContainerClient createBlobContainerIfNotExist(String containerBlobName);
    String runImageBuildWorkFlow(Maze maze);
    String runYourContainer(Maze maze);
}
