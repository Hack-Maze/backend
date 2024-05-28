package hack.maze.service;

import hack.maze.entity.ProfilePageProgress;

import java.util.List;

public interface ProfilePageProgressService {
    void checkIfUserAlreadyEnrolledInThisPage(long profileId, long pageId);
    String createNewProfilePageProgress(ProfilePageProgress profilePageProgress);
    ProfilePageProgress getUserPageProgress(long profileId, long pageId);
    List<ProfilePageProgress> getProfilePagesProgressByProfileId(long profileId);
}
