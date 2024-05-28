package hack.maze.service.impl;

import hack.maze.service.AzureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureServiceImpl implements AzureService {
    @Override
    public String sendImageToAzure(MultipartFile image) {
        return "image";
    }
}
