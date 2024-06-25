package hack.maze.dto;

import lombok.Builder;

@Builder
public record MazePageDTO(long id, String title, String description , boolean isCompleted) {
}
