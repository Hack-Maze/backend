package hack.maze.dto;

import hack.maze.entity.Difficulty;
import hack.maze.entity.Page;
import hack.maze.entity.Tag;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MazeResponseDTO(Long id, String title, String description, String summary, LocalDateTime createdAt,
                              boolean visibility, String image, Difficulty difficulty, MazeProfileDTO author,
                              List<MazeProfileDTO> enrolledUsers, List<MazeProfileDTO> solvers, List<Tag> tags,
                              List<MazePageDTO> pages) {
}
