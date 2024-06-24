package hack.maze.service;

import java.io.IOException;
import java.util.List;

import hack.maze.dto.CreateProfileDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.ProfileResponseDTO;
import hack.maze.entity.Profile;

public interface ProfileService {
    String updateProfileDate(CreateProfileDTO createProfileDTO) throws IOException;
    Profile _getSingleProfile(String username);
    Profile _getSingleProfile(Long id);
    ProfileResponseDTO getSingleProfile(String username);
    ProfileResponseDTO getCurrentUserProfile();
    List<MazeSimpleDTO> getAllProfileCreatedMazes();
    List<MazeSimpleDTO> getAllProfileCreatedMazes(String username);
}
