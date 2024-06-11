package hack.maze.service.impl;

import hack.maze.dto.ProfileMazeProgressDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Profile;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfilePageProgress;
import hack.maze.entity.ProfileQuestionProgress;
import hack.maze.entity.Question;
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
import static hack.maze.constant.ApplicationConstant.SOLVE_POINTS;
import static hack.maze.mapper.ProfileMazeProgressMapper.*;
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
        ProfileQuestionProgress savedProfileQuestionProgress = profileQuestionProgressRepo.save(ProfileQuestionProgress
                .builder()
                .question(question)
                .solvedAt(LocalDateTime.now())
                .profilePageProgress(profilePageProgress)
                .build());
        profile.setLastQuestionSolvedAt(savedProfileQuestionProgress.getSolvedAt());
        profile.setRank(profile.getRank() + SOLVE_POINTS);
        return "User progress updated successfully";
    }

    private ProfilePageProgress createPageProgressIfNotExist(Profile profile, long pageId) {
        ProfilePageProgress pageProgress = profilePageProgressService.getUserPageProgress(profile.getId(), pageId);
        if (pageProgress == null) {
            Page page = pageService._getSinglePage(pageId);
            ProfilePageProgress profilePageProgress = ProfilePageProgress
                    .builder()
                    .profile(profile)
                    .numberOfSolvedQuestions(0)
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
        return fromProfilePageProgressToProfilePageProgressDTO(profilePageProgressService.getSingleProfilePagesProgressByProfileIdAndPageId(profile.getId(), pageId));
    }
}
