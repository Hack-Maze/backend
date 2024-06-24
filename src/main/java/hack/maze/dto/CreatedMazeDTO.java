package hack.maze.dto;

import lombok.Builder;

@Builder
public record CreatedMazeDTO(long id, String title, String image) {
}
