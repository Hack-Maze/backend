package hack.maze.dto;

import lombok.Builder;

@Builder
public record RegisterDTO(String username, String email, String password) {
}
