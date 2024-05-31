package hack.maze.service.impl;

import hack.maze.dto.CreateProfileDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.ProfileResponseDTO;
import hack.maze.entity.Profile;
import hack.maze.repository.ProfileRepo;
import hack.maze.service.AzureService;
import hack.maze.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeSimpleDTO;
import static hack.maze.mapper.MazeMapper.fromProfileToProfileResponseDTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepo profileRepo;
    private final AzureService azureService;

    @Override
    @Transactional
    public String updateProfileDate(CreateProfileDTO createProfileDTO) {
        Profile profile = _getSingleProfile(getCurrentUser().getUsername());
        if (createProfileDTO.bio() != null) {
            profile.setBio(createProfileDTO.bio());
        }
        if (createProfileDTO.image() != null) {
            profile.setImage(azureService.sendImageToAzure(createProfileDTO.image()));
        }
        if (createProfileDTO.job() != null) {
            profile.setJob(createProfileDTO.job());
        }
        if (createProfileDTO.fullName() != null) {
            profile.setFullName(createProfileDTO.fullName());
        }
        if (createProfileDTO.country() != null) {
            profile.setCountry(createProfileDTO.country());
        }
        if (createProfileDTO.githubLink() != null) {
            profile.setGithubLink(createProfileDTO.githubLink());
        }
        if (createProfileDTO.linkedinLink() != null) {
            profile.setLinkedinLink(createProfileDTO.linkedinLink());
        }
        if (createProfileDTO.personalWebsite() != null) {
            profile.setPersonalWebsite(createProfileDTO.personalWebsite());
        }
        return "User profile updated successfully";
    }

    @Override
    public Profile _getSingleProfile(String username) {
        return profileRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Profile with username = [" + username + "] not exist"));
    }

    @Override
    public ProfileResponseDTO getSingleProfile(String username) {
        return fromProfileToProfileResponseDTO(_getSingleProfile(username));
    }

    @Override
    public List<MazeSimpleDTO> getAllProfileCreatedMazes() {
        Profile profile = _getSingleProfile(getCurrentUser().getUsername());
        return fromMazeToMazeSimpleDTO(profile.getCreatedMazes());
    }

}
