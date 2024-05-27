package hack.maze.service;

import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfilePageProgress;

import java.util.List;

public interface ProgressService {
    String enrollUserToMaze(long mazeId);
    String recordUserProgressInPage(long pageId);
    String solveQuestion(long pageId, long solvedQuestionId);
    List<ProfileMazeProgress> getProfileMazesProgress();
    List<ProfilePageProgress> getProfilePagesProgress();
}
