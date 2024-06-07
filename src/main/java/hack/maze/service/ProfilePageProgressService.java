package hack.maze.service;

import hack.maze.entity.ProfilePageProgress;

import java.util.List;

public interface ProfilePageProgressService {
    ProfilePageProgress _createNewProfilePageProgress(ProfilePageProgress profilePageProgress);
    ProfilePageProgress getUserPageProgress(long profileId, long pageId);
    List<ProfilePageProgress> getProfilePagesProgressByProfileId(long profileId);
}
