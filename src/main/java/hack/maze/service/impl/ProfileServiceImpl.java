package hack.maze.service.impl;

import hack.maze.dto.CreateProfileDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.ProfileResponseDTO;
import hack.maze.entity.AppUser;
import hack.maze.entity.Profile;
import hack.maze.repository.ProfileRepo;
import hack.maze.service.AzureService;
import hack.maze.service.ProfileService;
import hack.maze.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeSimpleDTO;
import static hack.maze.mapper.MazeMapper.fromProfileToProfileResponseDTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepo profileRepo;
    private final AzureService azureService;
    private final UserService userService;

    @Override
    @Transactional
    public String updateProfileDate(CreateProfileDTO createProfileDTO) {
        Profile profile = _getSingleProfile(getCurrentUser());
        AppUser appUser = profile.getAppUser();
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
        if (createProfileDTO.email() != null && !Objects.equals(appUser.getEmail(), createProfileDTO.email())) {
            userService.checkIfUserWithEmailExists(createProfileDTO.email());
            appUser.setEmail(createProfileDTO.email());
        }
        if (createProfileDTO.username() != null && !Objects.equals(appUser.getUsername(), createProfileDTO.username())) {
            userService.checkIfUserWithUsernameExists(createProfileDTO.username());
            appUser.setUsername(createProfileDTO.username());
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
        Profile profile = _getSingleProfile(getCurrentUser());
        return fromMazeToMazeSimpleDTO(profile.getCreatedMazes());
    }

}
