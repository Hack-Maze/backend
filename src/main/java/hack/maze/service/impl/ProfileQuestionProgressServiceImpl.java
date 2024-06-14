package hack.maze.service.impl;

import hack.maze.dto.MazeProfileDTO;
import hack.maze.dto.ProfileLeaderboardDTO;
import hack.maze.entity.Profile;
import hack.maze.entity.ProfileQuestionProgress;
import hack.maze.mapper.ProfileMapper;
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

import static hack.maze.mapper.ProfileMapper.fromProfileToMazeProfileDTO;

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
                .map(entry -> ProfileLeaderboardDTO.builder().mazeProfileDTO(fromProfileToMazeProfileDTO(entry.getKey())).score(entry.getValue()).build())
                .sorted(Comparator.comparingInt(ProfileLeaderboardDTO::score).reversed())
                .toList();
    }
}
