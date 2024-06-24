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
    public AzureContainer getSingleAzureContainer(String resourceGroupName) {
        return azureContainerRepo.findByResourceGroupName(resourceGroupName).orElseThrow(() -> new RuntimeException("No Azure Container found for resourceGroupName: " + resourceGroupName));
    }

    @Override
    public void saveAzureContainer(AzureContainer azureContainer) {
        azureContainerRepo.save(azureContainer);
    }

    @Override
    public void deleteAzureContainer(String resourceGroupName) {
        AzureContainer ac = getSingleAzureContainer(resourceGroupName);
        azureContainerRepo.delete(ac);
    }
}
