package hack.maze.dto;

import lombok.Builder;

@Builder
public record MazeProfileDTO(Long profileId, String username, String image) {
}
