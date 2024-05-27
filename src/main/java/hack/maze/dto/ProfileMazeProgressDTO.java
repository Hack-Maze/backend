package hack.maze.dto;

import lombok.Builder;

@Builder
public record ProfileMazeProgressDTO(long id, MazeProfileDTO profile, MazeProgressDTO maze, boolean isCompleted) {
}
