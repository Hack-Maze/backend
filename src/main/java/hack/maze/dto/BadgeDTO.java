package hack.maze.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record BadgeDTO(String title, MultipartFile image) {
}
