package hack.maze.dto;

import lombok.Builder;

@Builder
public record PageRequestDTO(String title, String description, String content) {
}
