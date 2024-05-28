package hack.maze.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileResponseDTO(long id, String username, String email, String fullName, String country, String image,
                                 double rank, String bio, String githubLink, String linkedinLink,
                                 String personalWebsite, String job, LocalDateTime lastQuestionSolvedAt) {
}
