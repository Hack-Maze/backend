package hack.maze.controller;

import hack.maze.service.AzureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/azure")
@RequiredArgsConstructor
public class AzureTestController {

    private final AzureService azureService;

    @PutMapping
    public String test(@RequestParam("file") MultipartFile image) throws IOException {
        return azureService.sendImageToAzure(image, "", 1L);
    }

}
