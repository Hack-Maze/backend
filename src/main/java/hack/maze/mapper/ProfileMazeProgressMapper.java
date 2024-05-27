package hack.maze.mapper;

import hack.maze.dto.ProfileMazeProgressDTO;
import hack.maze.entity.ProfileMazeProgress;

import java.util.List;
import java.util.stream.Collectors;

import static hack.maze.mapper.MazeMapper.fromMazeToMazeProgressDTO;
import static hack.maze.mapper.ProfileMapper.fromProfileToMazeProfileDTO;

public class ProfileMazeProgressMapper {

    public static List<ProfileMazeProgressDTO> fromProfileMazeProgressToProfileMazeProgressDTO(List<ProfileMazeProgress> profileMazeProgresses) {
        return profileMazeProgresses.stream().map(ProfileMazeProgressMapper::fromProfileMazeProgressToProfileMazeProgressDTO).collect(Collectors.toList());
    }

    public static ProfileMazeProgressDTO fromProfileMazeProgressToProfileMazeProgressDTO(ProfileMazeProgress profileMazeProgress) {
        return ProfileMazeProgressDTO
                .builder()
                .id(profileMazeProgress.getId())
                .profile(fromProfileToMazeProfileDTO(profileMazeProgress.getProfile()))
                .maze(fromMazeToMazeProgressDTO(profileMazeProgress.getMaze()))
                .isCompleted(profileMazeProgress.isCompleted())
                .build();
    }

}
