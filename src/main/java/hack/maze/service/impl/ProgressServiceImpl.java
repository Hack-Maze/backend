package hack.maze.service.impl;

import hack.maze.dto.LeaderboardMazeDTO;
import hack.maze.dto.ProfileLeaderboardDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.*;
import hack.maze.exceptions.ResourceAlreadyExistException;
import hack.maze.mapper.MazeMapper;
import hack.maze.mapper.ProgressMapper;
import hack.maze.repository.ProfileMazeProgressRepo;
import hack.maze.repository.ProfilePageProgressRepo;
import hack.maze.repository.QuestionProgressRepo;
import hack.maze.service.MazeService;
import hack.maze.service.PageService;
import hack.maze.service.ProfileService;
import hack.maze.service.ProgressService;
import hack.maze.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static hack.maze.config.UserContext.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final MazeService mazeService;
    private final PageService pageService;
    private final ProfileService profileService;
    private final QuestionService questionService;
    private final QuestionProgressRepo questionProgressRepo;
    private final ProfileMazeProgressRepo profileMazeProgressRepo;
    private final ProfilePageProgressRepo profilePageProgressRepo;


    @Override
    @Transactional
    public String enrollInMaze(long mazeId) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        Maze maze = mazeService._getSingleMaze(mazeId);
        checkIfUserAlreadyEnrolled(profile.getId(), maze.getId());
        createProfileMazeProgress(profile, maze);
        updateEnrolledUsers(profile, maze);
        return "Profile enrolled Successfully";
    }

    @Override
    @Transactional
    public String solveQuestion(long questionId, String answer) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        Question question = questionService._getSingleQuestion(questionId);
        Page page = question.getPage();
        ProfileMazeProgress pmp = profileMazeProgressRepo.findByProfileIdAndMazeId(profile.getId(), page.getMaze().getId()).orElseThrow(() -> new RuntimeException("User not enrolled to this maze yet"));
        ProfilePageProgress ppp = findOrCreateProfilePageProgress(profile, page, pmp, false);
        checkIfUserAlreadySolveThisQuestion(profile.getId(), question.getId());
        checkAnswer(question.getAnswer(), answer);
        QuestionProgress qp = createQuestionProgress(profile, question, ppp);
        updateProfileInfo(profile, question.getPoints(), qp.getSolvedAt());
        updatePageCompletionStatus(ppp, page);
        updateMazeCompletion(profile, pmp, page.getMaze().getPages().size());
        return "User progress updated Successfully";
    }

    @Transactional
    protected void updateMazeCompletion(Profile profile, ProfileMazeProgress pmp, int totalNumberOfPages) {
        if (pmp.getProfilePageProgresses().size() == totalNumberOfPages) {
            pmp.setCompleted(true);
            Maze maze = pmp.getMaze();
            List<Profile> solvers = maze.getSolvers();
            solvers.add(profile);
            maze.setSolvers(solvers);
            profile.setCompletedMazes(profile.getCompletedMazes() + 1);
        }
    }

    @Override
    public ProfilePageProgressDTO getAllSolvedQuestionsInPage(Long pageId) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        Optional<ProfilePageProgress> pageProgress = profilePageProgressRepo.findByProfileIdAndPageId(profile.getId(), pageId);
        return pageProgress.map(ProgressMapper::fromProfilePageProgressToPageProgressDTO).orElse(null);
    }

    @Override
    @Transactional
    public String markPageAsCompleted(Long pageId) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        Page page = pageService._getSinglePage(pageId);
        ProfileMazeProgress pmp = profileMazeProgressRepo.findByProfileIdAndMazeId(profile.getId(), page.getMaze().getId()).orElseThrow(() -> new RuntimeException("User not enrolled to this maze yet"));
        if (page.getQuestions().isEmpty()) {
            ProfilePageProgress ppp = findOrCreateProfilePageProgress(profile, page, pmp, true);
            ppp.setCompleted(true);
            ppp.setCompletedAt(LocalDateTime.now());
            updateMazeCompletion(profile, pmp, page.getMaze().getPages().size());
            return "Page with id = [" + pageId + "] marked as completed";
        } else {
            return "Can't mark this page as completed, you should solve the questions first";
        }
    }

    @Override
    // @Cacheable("Leaderboard") commented out to avoid caching and to make it real-time leaderboard
    public List<ProfileLeaderboardDTO> getLeaderboard(LocalDate start, LocalDate end) {
        List<QuestionProgress> questionProgresses = questionProgressRepo.findBySolvedAtBetween(start, end);
        Map<Profile, Integer> profileScores = new HashMap<>();
        for (QuestionProgress questionProgress : questionProgresses) {
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
                            .solvedMazesNumber(profile.getCompletedMazes())
                            .solvedMazes(getProfileSolvedMazes(profile.getId()))
                            .score(entry.getValue())
                            .build();
                })
                .sorted(Comparator.comparingInt(ProfileLeaderboardDTO::score).reversed())
                .toList();
    }

    @Override
public Map<LocalDate, Long> getCurrentUserProgressThisWeek() {
    Profile profile = profileService._getSingleProfile(getCurrentUser());
    LocalDate start = LocalDate.now().minusDays(6); // Start from 6 days ago to include today and make a full week
    LocalDate end = LocalDate.now();
    List<QuestionProgress> questionProgresses = questionProgressRepo.findBySolvedAtBetweenAndProfileId(start, end, profile.getId());

    // Initialize a map to hold the count of solved questions per day
    Map<LocalDate, Long> solvedQuestionsPerDay = IntStream.rangeClosed(0, 6)
            .mapToObj(start::plusDays)
            .collect(Collectors.toMap(Function.identity(), date -> 0L));

    // Populate the map with actual counts
    questionProgresses.forEach(questionProgress -> {
        LocalDate solvedDate = questionProgress.getSolvedAt().toLocalDate();
        solvedQuestionsPerDay.computeIfPresent(solvedDate, (date, count) -> count + 1);
    });

    return solvedQuestionsPerDay;
}


    @Override
    public List<LeaderboardMazeDTO> notCompletedMazes() {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        return MazeMapper.fromMazeToMazeToLeaderboardMazeDTO(profileMazeProgressRepo.notCompletedMazes(profile.getId()));
    }

    private List<LeaderboardMazeDTO> getProfileSolvedMazes(Long profileId) {
        return mazeService.getSolvedMazesByProfileId(profileId);
    }

    private void checkAnswer(String correctAnswer, String userAnswer) {
        if (!Objects.equals(correctAnswer.toLowerCase(), userAnswer.toLowerCase())) {
            throw new RuntimeException("Wrong answer");
        }
    }

    @Transactional
    protected void updatePageCompletionStatus(ProfilePageProgress ppp, Page page) {
        int solvedQuestions = ppp.getQuestionProgresses() != null ? ppp.getQuestionProgresses().size() : 0;
        int totalQuestions = page.getQuestions().size();
        if (solvedQuestions == totalQuestions) {
            ppp.setCompleted(true);
            ppp.setCompletedAt(LocalDateTime.now());
        }
    }

    private ProfilePageProgress findOrCreateProfilePageProgress(Profile profile, Page page, ProfileMazeProgress pmp, boolean isCompleted) {
        return profilePageProgressRepo.findByProfileIdAndPageId(profile.getId(), page.getId()).orElseGet(() -> profilePageProgressRepo
                .save(ProfilePageProgress
                        .builder()
                        .profile(profile)
                        .page(page)
                        .isCompleted(isCompleted)
                        .questionProgresses(new ArrayList<>())
                        .profileMazeProgress(pmp)
                        .build()));
    }

    @Transactional
    protected void updateProfileInfo(Profile profile, int points, LocalDateTime solvedAt) {
        profile.setRank(profile.getRank() + points);
        profile.setLastQuestionSolvedAt(solvedAt);
        profile.setLevel(calcUserLevelBasedOnCurrentRank(profile.getRank()));
    }

    private Level calcUserLevelBasedOnCurrentRank(int rank) {
        for (int i = 0; i < Level.values().length; i++) {
            if (rank <= Level.NOOB.getValue()) {
                return Level.NOOB;
            }
            if (rank <= Level.values()[i].getValue()) {
                return Level.values()[i - 1];
            }
        }
        return Level.SUPERIOR;
    }


    private QuestionProgress createQuestionProgress(Profile profile, Question question, ProfilePageProgress ppp) {
        QuestionProgress qp = questionProgressRepo.save(QuestionProgress
                .builder()
                .question(question)
                .profile(profile)
                .solvedAt(LocalDateTime.now())
                .profilePageProgress(ppp)
                .build());
        List<QuestionProgress> qps = ppp.getQuestionProgresses();
        qps.add(qp);
        ppp.setQuestionProgresses(qps);
        return qp;
    }

    private void checkIfUserAlreadySolveThisQuestion(Long profileId, Long questionId) {
        if (questionProgressRepo.findByProfileIdAndQuestionId(profileId, questionId).isPresent()) {
            throw new ResourceAlreadyExistException("Profile already solved this question");
        }
    }

    @Transactional
    protected void updateEnrolledUsers(Profile profile, Maze maze) {
        List<Profile> enrolledUsers = maze.getEnrolledUsers();
        enrolledUsers.add(profile);
        maze.setEnrolledUsers(enrolledUsers);
    }

    private void checkIfUserAlreadyEnrolled(Long profileId, Long mazeId) {
        if (profileMazeProgressRepo.findByProfileIdAndMazeId(profileId, mazeId).isPresent()) {
            throw new ResourceAlreadyExistException("Profile already enrolled");
        }
    }

    private void createProfileMazeProgress(Profile profile, Maze maze) {
        profileMazeProgressRepo.save(ProfileMazeProgress
                .builder()
                .profile(profile)
                .maze(maze)
                .enrolledAt(LocalDateTime.now())
                .build());
    }

    // type getCurrentLevelProgress method takes no arguments and returns json like the following:
    // {
    // "currentLevel": "NOOB",
    // "points": 1000,
    // "nextLevelPoints": 2000

// }
    @Override
    public Map<String, Object> getCurrentLevelProgress() {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        Level currentLevel = profile.getLevel();
        Level nextLevel = Level.values()[currentLevel.ordinal() + 1];
        int nextLevelPoints = nextLevel.getValue();
        return Map.of(
                "currentLevel", currentLevel.name(),
                "points", profile.getRank(),
                "nextLevelPoints", nextLevelPoints
        );
    }
}
