package hack.maze.dto;

import lombok.Builder;

@Builder
public record CreateMazeDTO(String title, String description, String summary) {
}
