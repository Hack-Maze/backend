package hack.maze.service;

import hack.maze.dto.ProfileMazeProgressDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.ProfilePageProgress;

import java.util.List;

public interface ProgressService {
    String enrollUserToMaze(long mazeId);
    String recordUserProgressInPage(long pageId);
    String solveQuestion(long pageId, long solvedQuestionId);
    List<ProfileMazeProgressDTO> getProfileMazesProgress();
    List<ProfilePageProgressDTO> getProfilePagesProgress();
}
