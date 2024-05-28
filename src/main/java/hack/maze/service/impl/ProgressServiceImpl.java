package hack.maze.service.impl;

import hack.maze.config.UserContext;
import hack.maze.dto.ProfileMazeProgressDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Profile;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfilePageProgress;
import hack.maze.entity.ProfileQuestionProgress;
import hack.maze.entity.Question;
import hack.maze.mapper.ProfileMazeProgressMapper;
import hack.maze.mapper.ProfilePageProgressMapper;
import hack.maze.repository.ProfileQuestionProgressRepo;
import hack.maze.service.MazeService;
import hack.maze.service.PageService;
import hack.maze.service.ProfileMazeProgressService;
import hack.maze.service.ProfilePageProgressService;
import hack.maze.service.ProgressService;
import hack.maze.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static hack.maze.constant.ApplicationConstant.SOLVE_POINTS;
import static hack.maze.mapper.ProfileMazeProgressMapper.*;
import static hack.maze.mapper.ProfilePageProgressMapper.fromProfilePageProgressToProfilePageProgressDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final MazeService mazeService;
    private final PageService pageService;
    private final QuestionService questionService;
    private final ProfileMazeProgressService profileMazeProgressService;
    private final ProfilePageProgressService profilePageProgressService;
    private final ProfileQuestionProgressRepo profileQuestionProgressRepo;

    @Override
    public String enrollUserToMaze(long mazeId) {
        Profile profile = UserContext.getCurrentUser().getProfile();
        profileMazeProgressService.checkIfUserAlreadyEnrolledInThisMaze(profile.getId(), mazeId);
        Maze maze = mazeService._getSingleMaze(mazeId);
        ProfileMazeProgress profileMazeProgress = ProfileMazeProgress
                .builder()
                .maze(maze)
                .profile(profile)
                .isCompleted(false)
                .build();
        String ret = profileMazeProgressService.createNewProfileMazeProgress(profileMazeProgress);
        return "User enrolled in maze with title = [" + maze.getTitle() + "] successfully | " + ret;
    }

    @Override
    public String recordUserProgressInPage(long pageId) {
        Profile profile = UserContext.getCurrentUser().getProfile();
        profilePageProgressService.checkIfUserAlreadyEnrolledInThisPage(profile.getId(), pageId);
        Page page = pageService.getSinglePage(pageId);
        ProfilePageProgress profilePageProgress = ProfilePageProgress
                .builder()
                .profile(profile)
                .numberOfSolvedQuestions(0)
                .isCompleted(false)
                .page(page)
                .build();
        String ret = profilePageProgressService.createNewProfilePageProgress(profilePageProgress);
        return "Page progress added successfully | " + ret;
    }

    @Override
    @Transactional
    public String solveQuestion(long pageId, long solvedQuestionId) {
        Profile profile = UserContext.getCurrentUser().getProfile();
        ProfilePageProgress profilePageProgress = profilePageProgressService.getUserPageProgress(profile.getId(), pageId);
        Question question = questionService.getSingleQuestion(solvedQuestionId);
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

    @Override
    public List<ProfileMazeProgressDTO> getProfileMazesProgress() {
        return fromProfileMazeProgressToProfileMazeProgressDTO(profileMazeProgressService.getProfileMazesProgressByProfileId(UserContext.getCurrentUser().getProfile().getId()));
    }

    @Override
    public List<ProfilePageProgressDTO> getProfilePagesProgress() {
        return fromProfilePageProgressToProfilePageProgressDTO(profilePageProgressService.getProfilePagesProgressByProfileId(UserContext.getCurrentUser().getProfile().getId()));
    }
}
