package hack.maze.service.impl;

import hack.maze.dto.ProfileMazeProgressDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.*;
import hack.maze.repository.ProfileQuestionProgressRepo;
import hack.maze.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.mapper.ProfileMazeProgressMapper.fromProfileMazeProgressToProfileMazeProgressDTO;
import static hack.maze.mapper.ProfilePageProgressMapper.fromProfilePageProgressToProfilePageProgressDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final ProfileService profileService;
    private final MazeService mazeService;
    private final PageService pageService;
    private final QuestionService questionService;
    private final ProfileMazeProgressService profileMazeProgressService;
    private final ProfilePageProgressService profilePageProgressService;
    private final ProfileQuestionProgressRepo profileQuestionProgressRepo;

    @Override
    @Transactional
    public String enrollUserToMaze(long mazeId) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        profileMazeProgressService.checkIfUserAlreadyEnrolledInThisMaze(profile.getId(), mazeId);
        Maze maze = mazeService._getSingleMaze(mazeId);
        updateEnrolledUserInMaze(maze, profile);
        ProfileMazeProgress profileMazeProgress = ProfileMazeProgress
                .builder()
                .maze(maze)
                .profile(profile)
                .isCompleted(false)
                .build();
        String ret = profileMazeProgressService.createNewProfileMazeProgress(profileMazeProgress);
        return "User enrolled in maze with title = [" + maze.getTitle() + "] successfully | " + ret;
    }

    @Transactional
    protected void updateEnrolledUserInMaze(Maze maze, Profile profile) {
        List<Profile> enrolledUsers = maze.getEnrolledUsers();
        enrolledUsers.add(profile);
        maze.setEnrolledUsers(enrolledUsers);
    }

    @Override
    @Transactional
    public String solveQuestion(long pageId, long solvedQuestionId, String answer) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        ProfilePageProgress profilePageProgress = createPageProgressIfNotExist(profile, pageId);
        Question question = questionService._getSingleQuestion(solvedQuestionId);
        checkAnswer(question.getAnswer(), answer);
        ProfileQuestionProgress savedProfileQuestionProgress = saveQuestionProgress(question, profilePageProgress);
        updateProfileInfo(profile, savedProfileQuestionProgress, question);
        updateProfilePageProgressInfo(profilePageProgress);
        checkMazeCompletion(question.getPage().getMaze().getId());
        return "User progress updated successfully";
    }

    @Transactional
    protected ProfileQuestionProgress saveQuestionProgress(Question question, ProfilePageProgress profilePageProgress) {
        return profileQuestionProgressRepo.save(ProfileQuestionProgress
                .builder()
                .question(question)
                .solvedAt(LocalDateTime.now())
                .profilePageProgress(profilePageProgress)
                .build());
    }

    @Transactional
    protected void updateProfileInfo(Profile profile, ProfileQuestionProgress savedProfileQuestionProgress, Question question) {
        profile.setLastQuestionSolvedAt(savedProfileQuestionProgress.getSolvedAt());
        profile.setRank(profile.getRank() + question.getPoints());
        profile.setLevel(calcUserLevelBasedOnCurrentRank(profile.getRank()));
    }


    @Transactional
    protected void updateProfilePageProgressInfo(ProfilePageProgress profilePageProgress) {
        profilePageProgress.setNumberOfSolvedQuestions(profilePageProgress.getNumberOfSolvedQuestions() + 1);
        profilePageProgress.setCompleted(profilePageProgress.getPage().getQuestions().size() == profilePageProgress.getNumberOfSolvedQuestions());
    }

    @Transactional
    protected void checkMazeCompletion(Long id) {
        ProfileMazeProgress profileMazeProgress = profileMazeProgressService.getProfileMazeProgressByMazeId(id);
        int totalPages = profileMazeProgress.getMaze().getPages().size();
        int solvedPages = 0;
        for (ProfilePageProgress ppp : profileMazeProgress.getProfilePageProgresses()) {
            if (ppp.isCompleted()) solvedPages += 1;
        }
        profileMazeProgress.setCompleted(totalPages == solvedPages);
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

    private ProfilePageProgress createPageProgressIfNotExist(Profile profile, long pageId) {
        ProfilePageProgress pageProgress = profilePageProgressService.getUserPageProgress(profile.getId(), pageId);
        if (pageProgress == null) {
            Page page = pageService._getSinglePage(pageId);
            ProfileMazeProgress profileMazeProgress = profileMazeProgressService.getProfileMazeProgressByMazeId(page.getMaze().getId());
            ProfilePageProgress profilePageProgress = ProfilePageProgress
                    .builder()
                    .profile(profile)
                    .numberOfSolvedQuestions(0)
                    .mazeProgress(profileMazeProgress)
                    .isCompleted(false)
                    .page(page)
                    .build();
            return profilePageProgressService._createNewProfilePageProgress(profilePageProgress);
        }
        return pageProgress;
    }

    private ProfilePageProgress createPageProgressIfNotExist(Profile profile, Page page) {
        ProfilePageProgress pageProgress = profilePageProgressService.getUserPageProgress(profile.getId(), page.getId());
        if (pageProgress == null) {
            ProfileMazeProgress profileMazeProgress = profileMazeProgressService.getProfileMazeProgressByMazeId(page.getMaze().getId());
            ProfilePageProgress profilePageProgress = ProfilePageProgress
                    .builder()
                    .profile(profile)
                    .numberOfSolvedQuestions(0)
                    .mazeProgress(profileMazeProgress)
                    .isCompleted(false)
                    .page(page)
                    .build();
            return profilePageProgressService._createNewProfilePageProgress(profilePageProgress);
        }
        return pageProgress;
    }

    private void checkAnswer(String theActualAnswer, String userAnswer) {
        if (!Objects.equals(theActualAnswer.toLowerCase(), userAnswer.toLowerCase())) {
            throw new RuntimeException("Wrong answer");
        }
    }

    @Override
    public List<ProfileMazeProgressDTO> getProfileMazesProgress() {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        return fromProfileMazeProgressToProfileMazeProgressDTO(profileMazeProgressService.getProfileMazesProgressByProfileId(profile.getId()));
    }

    @Override
    public List<ProfilePageProgressDTO> getProfilePagesProgress() {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        return fromProfilePageProgressToProfilePageProgressDTO(profilePageProgressService.getProfilePagesProgressByProfileId(profile.getId()));
    }

    @Override
    public ProfilePageProgressDTO getProfilePagesProgressInSinglePage(long pageId) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        return fromProfilePageProgressToProfilePageProgressDTO(profilePageProgressService.getUserPageProgress(profile.getId(), pageId));
    }

    @Override
    public String markPageAsCompleted(long pageId) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        Page page = pageService._getSinglePage(pageId);
        if (page.getQuestions().isEmpty()) {
            createPageProgressIfNotExist(profile, page);
            return "Page with id = [" + pageId + "] mark as completed";
        } else {
            return "Can't mark this page as completed, you should solve the questions first";
        }
    }
}
