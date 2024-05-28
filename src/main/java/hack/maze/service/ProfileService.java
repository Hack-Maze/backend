package hack.maze.service;

import hack.maze.dto.CreateProfileDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.ProfileResponseDTO;
import hack.maze.entity.Profile;

import java.util.List;

public interface ProfileService {
    String updateProfileDate(CreateProfileDTO createProfileDTO);
    ProfileResponseDTO getSingleProfile(long profileId);
    Profile _getSingleProfile(long profileId);
    Profile _getSingleProfile(String username);
    ProfileResponseDTO getSingleProfile(String username);
    List<MazeSimpleDTO> getAllProfileCreatedMazes();
}
