package hack.maze.mapper;

import hack.maze.dto.MazeProfileDTO;
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
        log.info("ProfileId: {}", profile.getId());
        return MazeProfileDTO
                .builder()
                .profileId(profile.getId())
                .username(profile.getAppUser().getUsername())
                .fullName(profile.getFullName())
                .country(profile.getCountry())
                .linkedinLink(profile.getLinkedinLink())
                .githubLink(profile.getGithubLink())
                .personalWebsite(profile.getPersonalWebsite())
                .job(profile.getJob())
                .image(profile.getImage())
                .rank(profile.getRank())
                .build();
    }

}
