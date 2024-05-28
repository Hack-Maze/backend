package hack.maze.dto;

import hack.maze.entity.Role;
import lombok.Builder;

@Builder
public record AuthenticationResponseDTO(String token, Role role, String username) {
}
