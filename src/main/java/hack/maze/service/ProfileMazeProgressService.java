package hack.maze.service;

import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfilePageProgress;

public interface ProfileMazeProgressService {
    void checkIfUserAlreadyEnrolledInThisMaze(long profileId, long mazeId);
    String createNewProfileMazeProgress(ProfileMazeProgress profileMazeProgress);
}
