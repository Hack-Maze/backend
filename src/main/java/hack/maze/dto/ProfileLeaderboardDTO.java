package hack.maze.dto;

import hack.maze.entity.Level;
import lombok.Builder;

@Builder
public record ProfileLeaderboardDTO(Long profileId, String username, String image, int score, Level level, String country, int solvedMazes) {
}
