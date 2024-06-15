package hack.maze.dto;

import hack.maze.entity.Difficulty;
import lombok.Builder;

@Builder
public record LeaderboardMazeDTO(Long mazeId, String title, String image, Difficulty difficulty) {
}
