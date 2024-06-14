package hack.maze.dto;

import lombok.Builder;

@Builder
public record ProfileLeaderboardDTO(Long profileId, String username, String image, int score) {
}
