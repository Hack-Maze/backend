package hack.maze.mapper;

import hack.maze.dto.MazeProfileDTO;
import hack.maze.dto.ProfileResponseDTO;
import hack.maze.entity.AppUser;
import hack.maze.entity.Profile;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProfileMapper {

    public static List<MazeProfileDTO> fromProfileToMazeProfileDTO(List<Profile> profiles) {
        List<MazeProfileDTO> mazeProfileDTOS = new ArrayList<>();
        for (Profile profile : profiles) {
            mazeProfileDTOS.add(fromProfileToMazeProfileDTO(profile));
        }
        return mazeProfileDTOS;
    }

    public static MazeProfileDTO fromProfileToMazeProfileDTO(Profile profile) {
        return MazeProfileDTO
                .builder()
                .profileId(profile.getId())
                .username(profile.getAppUser().getUsername())
                .image(profile.getImage())
                .build();
    }

    public static ProfileResponseDTO fromProfileToProfileResponseDTO(Profile profile) {
        AppUser appUser = profile.getAppUser();
        return ProfileResponseDTO
                .builder()
                .id(profile.getId())
                .username(appUser.getUsername())
                .email(appUser.getEmail())
                .fullName(profile.getFullName())
                .country(profile.getCountry())
                .image(profile.getImage())
                .rank(profile.getRank())
                .bio(profile.getBio())
                .githubLink(profile.getGithubLink())
                .linkedinLink(profile.getLinkedinLink())
                .personalWebsite(profile.getPersonalWebsite())
                .job(profile.getJob())
                .lastQuestionSolvedAt(profile.getLastQuestionSolvedAt())
                .badges(profile.getBadges())
                .level(profile.getLevel())
                .build();
    }

}
