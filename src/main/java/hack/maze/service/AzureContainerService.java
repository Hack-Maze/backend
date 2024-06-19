package hack.maze.service;

import hack.maze.entity.AzureContainer;

public interface AzureContainerService {
    AzureContainer getSingleAzureContainer(long mazeId);
    AzureContainer saveAzureContainer(AzureContainer azureContainer);
}
