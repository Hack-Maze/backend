package hack.maze.dto;

import lombok.Builder;

@Builder
public record ProfileLeaderboardDTO(MazeProfileDTO mazeProfileDTO, int score) {
}
