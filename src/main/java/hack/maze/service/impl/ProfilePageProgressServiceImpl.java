package hack.maze.service.impl;

import hack.maze.entity.ProfilePageProgress;
import hack.maze.exceptions.ResourceAlreadyExistException;
import hack.maze.repository.ProfilePageProgressRepo;
import hack.maze.service.ProfilePageProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfilePageProgressServiceImpl implements ProfilePageProgressService {

    private final ProfilePageProgressRepo profilePageProgressRepo;

    @Override
    public void checkIfUserAlreadyEnrolledInThisPage(long profileId, long pageId) {
        if (profilePageProgressRepo.findByProfileIdAndPageId(profileId, pageId).isPresent()) {
            throw new ResourceAlreadyExistException("User Already enrolled in this Page before");
        }
    }

    @Override
    public String createNewProfilePageProgress(ProfilePageProgress profilePageProgress) {
        profilePageProgressRepo.save(profilePageProgress);
        return "Profile progress updated successfully";
    }

    @Override
    public ProfilePageProgress getUserPageProgress(long profileId, long pageId) {
        return profilePageProgressRepo.findByProfileIdAndPageId(profileId, pageId).orElseThrow(() -> new RuntimeException("You should add your progress in this Page first!"));
    }
}
