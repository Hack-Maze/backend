package hack.maze.service;

import hack.maze.entity.AzureContainer;

public interface AzureContainerService {
    AzureContainer getSingleAzureContainer(String resourceGroupName);
    void saveAzureContainer(AzureContainer azureContainer);
    void deleteAzureContainer(String resourceGroupName);
}
