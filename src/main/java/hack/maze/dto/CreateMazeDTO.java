package hack.maze.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record CreateMazeDTO(String title, String description, String summary, List<Long> tagIds, MultipartFile image) {
}
