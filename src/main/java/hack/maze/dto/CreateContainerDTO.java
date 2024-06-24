package hack.maze.dto;

import java.util.List;

public record CreateContainerDTO(String username, String containerImage, String environmentVariables,
                                 List<String> openPorts, String mazeTitle) {
}
