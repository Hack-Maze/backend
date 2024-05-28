package hack.maze.service;

import hack.maze.dto.CreateMazeDTO;
import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.UpdateMazeDTO;
import hack.maze.entity.Maze;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface MazeService {
    String createMaze(CreateMazeDTO createMazeDTO);
    List<MazeSimpleDTO> getAllMazes();
    MazeResponseDTO getSingleMaze(long mazeId);
    Maze _getSingleMaze(long mazeId);
    String deleteMaze(long mazeId) throws AccessDeniedException;
    String updateMaze(long mazeId, UpdateMazeDTO updateMazeDTO) throws AccessDeniedException;
}
