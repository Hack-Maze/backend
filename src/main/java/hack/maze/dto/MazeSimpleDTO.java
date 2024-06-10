package hack.maze.dto;

import hack.maze.entity.Difficulty;
import hack.maze.entity.Profile;
import lombok.Builder;

@Builder
public record MazeSimpleDTO(long id, String title,
                            String description, String image,
                            String summary, Difficulty difficulty,
                            MazeProfileDTO author) {
}
