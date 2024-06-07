package hack.maze.service.impl;

import hack.maze.entity.ProfilePageProgress;
import hack.maze.exceptions.ResourceAlreadyExistException;
import hack.maze.repository.ProfilePageProgressRepo;
import hack.maze.service.ProfilePageProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfilePageProgressServiceImpl implements ProfilePageProgressService {

    private final ProfilePageProgressRepo profilePageProgressRepo;

    @Override
    public ProfilePageProgress _createNewProfilePageProgress(ProfilePageProgress profilePageProgress) {
        return profilePageProgressRepo.save(profilePageProgress);
    }

    @Override
    public ProfilePageProgress getUserPageProgress(long profileId, long pageId) {
        return profilePageProgressRepo.findByProfileIdAndPageId(profileId, pageId).orElse(null);
    }

    @Override
    public List<ProfilePageProgress> getProfilePagesProgressByProfileId(long profileId) {
        return profilePageProgressRepo.getProfilePagesProgressByProfileId(profileId);
    }
}
