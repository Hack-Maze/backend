package hack.maze.service;

import hack.maze.dto.LeaderboardMazeDTO;
import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.UpdateMazeDTO;
import hack.maze.entity.Maze;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface MazeService {
    Long createMaze(UpdateMazeDTO updateMazeDTO) throws IOException;
    List<MazeSimpleDTO> getAllMazes();
    MazeResponseDTO getSingleMaze(long mazeId);
    Maze _getSingleMaze(long mazeId);
    String deleteMaze(long mazeId) throws AccessDeniedException;
    String updateMaze(long mazeId, UpdateMazeDTO updateMazeDTO) throws IOException;
    boolean isCurrentUserEnrolledInMaze(long mazeId);
    boolean isCurrentUserSolverInMaze(long mazeId);
    List<LeaderboardMazeDTO> getSolvedMazesByProfileId(long profileId);
    String runAndBuildContainerFromMazeFile(long mazeId);
}
