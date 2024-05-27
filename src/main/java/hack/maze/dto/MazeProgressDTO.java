package hack.maze.dto;

import lombok.Builder;

@Builder
public record MazeProgressDTO(long id, String title, String image) {
}
