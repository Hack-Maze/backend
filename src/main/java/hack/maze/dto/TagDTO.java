package hack.maze.dto;

import lombok.Builder;

@Builder
public record TagDTO(long id, String title) {
}
