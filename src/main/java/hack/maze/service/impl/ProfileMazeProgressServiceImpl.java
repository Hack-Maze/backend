package hack.maze.service.impl;

import hack.maze.config.UserContext;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.exceptions.ResourceAlreadyExistException;
import hack.maze.repository.ProfileMazeProgressRepo;
import hack.maze.service.ProfileMazeProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileMazeProgressServiceImpl implements ProfileMazeProgressService {

    private final ProfileMazeProgressRepo profileMazeProgressRepo;

    @Override
    public void checkIfUserAlreadyEnrolledInThisMaze(long profileId, long mazeId) {
        if (profileMazeProgressRepo.findByProfileIdAndMazeId(profileId, mazeId).isPresent()) {
            throw new ResourceAlreadyExistException("User Already enrolled in this maze before");
        }
    }

    @Override
    public String createNewProfileMazeProgress(ProfileMazeProgress profileMazeProgress) {
        profileMazeProgressRepo.save(profileMazeProgress);
        return "Profile progress updated successfully";
    }
}
