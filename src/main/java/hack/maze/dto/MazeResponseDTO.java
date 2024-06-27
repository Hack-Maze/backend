package hack.maze.dto;

import hack.maze.entity.Difficulty;
import hack.maze.entity.Tag;
import hack.maze.entity.Type;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MazeResponseDTO(Long id, String title, String description, String summary, LocalDateTime createdAt,
                              boolean visibility, Type type , String image, String file, Difficulty difficulty, MazeProfileDTO author,
                              int numberOfEnrolledUsers, int numberOfSolvers, List<Tag> tags,
                              List<MazePageDTO> pages, int totalPoints) {
}
