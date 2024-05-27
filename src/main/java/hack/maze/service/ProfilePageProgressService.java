package hack.maze.service;

import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfilePageProgress;

public interface ProfilePageProgressService {
    void checkIfUserAlreadyEnrolledInThisPage(long profileId, long pageId);
    String createNewProfilePageProgress(ProfilePageProgress profilePageProgress);
    ProfilePageProgress getUserPageProgress(long profileId, long pageId);
}
