package hack.maze.dto;

import lombok.Builder;

import java.io.File;

@Builder
public record DockerfileAndUnzippedFolderDTO(File dockerfile, File unzippedFolder) {
}
