package hack.maze.service;

import org.springframework.web.multipart.MultipartFile;

public interface AzureService {
    String sendImageToAzure(MultipartFile image);
}
