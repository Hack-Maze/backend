package hack.maze.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateProfileDTO(String fullName, String country, MultipartFile image, String bio, String githubLink,
                               String linkedinLink, String personalWebsite, String job) {
}
