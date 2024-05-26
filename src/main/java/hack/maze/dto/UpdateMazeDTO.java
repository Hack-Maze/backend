package hack.maze.dto;

import hack.maze.entity.Difficulty;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record UpdateMazeDTO(String title, String description, String summary, List<Long> tagIds, MultipartFile image, Difficulty difficulty) {
}
