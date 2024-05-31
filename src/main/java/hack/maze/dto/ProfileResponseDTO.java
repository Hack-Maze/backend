package hack.maze.dto;

import hack.maze.entity.Badge;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProfileResponseDTO(long id, String username, String email, String fullName, String country, String image,
                                 double rank, String bio, String githubLink, String linkedinLink,
                                 String personalWebsite, String job, LocalDateTime lastQuestionSolvedAt, List<Badge> badges) {
}
