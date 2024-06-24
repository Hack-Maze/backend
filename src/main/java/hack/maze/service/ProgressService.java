package hack.maze.service;

import hack.maze.dto.LeaderboardMazeDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.ProfileLeaderboardDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.ProfileMazeProgress;
import java.util.Map;
import java.time.LocalDate;
import java.util.List;

public interface ProgressService {
    String enrollInMaze(long mazeId);
    String solveQuestion(long questionId, String answer);
    ProfilePageProgressDTO getAllSolvedQuestionsInPage(Long pageId);
    String markPageAsCompleted(Long pageId);
    List<ProfileLeaderboardDTO> getLeaderboard(LocalDate start, LocalDate end);
    List<LeaderboardMazeDTO> notCompletedMazes();
    Map<LocalDate, Long> getCurrentUserProgressThisWeek();
    Map<String, Object> getCurrentLevelProgress();
}
