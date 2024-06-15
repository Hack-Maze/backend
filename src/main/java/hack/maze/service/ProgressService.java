package hack.maze.service;

import hack.maze.dto.ProfileLeaderboardDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.ProfileMazeProgress;

import java.time.LocalDate;
import java.util.List;

public interface ProgressService {
    String enrollInMaze(long mazeId);
    String solveQuestion(long questionId, String answer);
    ProfilePageProgressDTO getAllSolvedQuestionsInPage(Long pageId);
    String markPageAsCompleted(Long pageId);
    List<ProfileLeaderboardDTO> getLeaderboard(LocalDate start, LocalDate end);
}
