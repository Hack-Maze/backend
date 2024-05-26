package hack.maze.dto;

import lombok.Builder;

@Builder
public record MazeProfileDTO(Long profileId, String username, String fullName, String country, String image,
                             double rank, String githubLink, String linkedinLink, String personalWebsite,
                             String job) {
}
