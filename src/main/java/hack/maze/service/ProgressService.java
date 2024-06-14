package hack.maze.service;

import hack.maze.dto.ProfileMazeProgressDTO;
import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.ProfileQuestionProgress;

import java.util.List;

public interface ProgressService {
    String enrollUserToMaze(long mazeId);
    String solveQuestion(long pageId, long solvedQuestionId, String answer);
    List<ProfileMazeProgressDTO> getProfileMazesProgress();
    List<ProfilePageProgressDTO> getProfilePagesProgress();
    ProfilePageProgressDTO getProfilePagesProgressInSinglePage(long pageId);
    String markPageAsCompleted(long pageId);
}
