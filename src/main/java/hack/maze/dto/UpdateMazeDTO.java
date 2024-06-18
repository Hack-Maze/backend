package hack.maze.dto;

import hack.maze.entity.Difficulty;
import hack.maze.entity.Type;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record UpdateMazeDTO(String title, String description, String summary, Boolean visibility, List<Long> tagIds, MultipartFile image, MultipartFile file, String type, Difficulty difficulty) {
}
