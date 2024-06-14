package hack.maze.service.impl;

import hack.maze.dto.ProfileLeaderboardDTO;
import hack.maze.entity.Profile;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfileQuestionProgress;
import hack.maze.repository.ProfileQuestionProgressRepo;
import hack.maze.service.ProfileQuestionProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileQuestionProgressServiceImpl implements ProfileQuestionProgressService {

    private final ProfileQuestionProgressRepo profileQuestionProgressRepo;

    @Override
    @Cacheable("Leaderboard")
    public List<ProfileLeaderboardDTO> getLeaderboard(LocalDate start, LocalDate end) {
        List<ProfileQuestionProgress> questionProgresses = profileQuestionProgressRepo.findBySolvedAtBetween(start, end);
        Map<Profile, Integer> profileScores = new HashMap<>();

        for (ProfileQuestionProgress questionProgress : questionProgresses) {
            Profile profile = questionProgress.getProfilePageProgress().getProfile();
            int points = questionProgress.getQuestion().getPoints();
            profileScores.put(profile, profileScores.getOrDefault(profile, 0) + points);
        }

        return profileScores.entrySet().stream()
                .map(entry -> {
                    Profile profile = entry.getKey();
                    return ProfileLeaderboardDTO
                            .builder()
                            .profileId(profile.getId())
                            .username(profile.getAppUser().getUsername())
                            .image(profile.getImage())
                            .level(profile.getLevel())
                            .country(profile.getCountry())
                            .solvedMazes(calcProfileSolvedMazes(profile.getProfileMazeProgresses()))
                            .score(entry.getValue())
                            .build();
                })
                .sorted(Comparator.comparingInt(ProfileLeaderboardDTO::score).reversed())
                .toList();
    }

    private int calcProfileSolvedMazes(List<ProfileMazeProgress> profileMazeProgresses) {
        int sum = 0;
        for (ProfileMazeProgress pmp: profileMazeProgresses) {
            if (pmp.isCompleted()) sum += 1;
        }
        return sum;
    }
}
