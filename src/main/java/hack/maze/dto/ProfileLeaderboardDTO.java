package hack.maze.dto;

import hack.maze.entity.Level;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileLeaderboardDTO(Long profileId, String username, String image, int score, Level level, String country, int solvedMazesNumber, List<LeaderboardMazeDTO> solvedMazes) {
}
