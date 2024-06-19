package hack.maze.service.impl;

import hack.maze.entity.AzureContainer;
import hack.maze.repository.AzureContainerRepo;
import hack.maze.service.AzureContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureContainerServiceImpl implements AzureContainerService {

    private final AzureContainerRepo azureContainerRepo;

    @Override
    public AzureContainer getSingleAzureContainer(long mazeId) {
        return azureContainerRepo.findByMazeId(mazeId).orElseThrow(() -> new RuntimeException("maze with id [" + mazeId + "] not found"));
    }

    @Override
    public AzureContainer saveAzureContainer(AzureContainer azureContainer) {
        return azureContainerRepo.save(azureContainer);
    }
}
