package hack.maze.mapper;

import hack.maze.dto.*;
import hack.maze.entity.Maze;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static hack.maze.mapper.PageMapper.fromPageToMazePageDTO;
import static hack.maze.mapper.ProfileMapper.fromProfileToMazeProfileDTO;

@Slf4j
public class MazeMapper {

    public static List<MazeResponseDTO> fromMazeToMazeResponseDTO(List<Maze> mazes) {
        return mazes.stream().map(MazeMapper::fromMazeToMazeResponseDTO).collect(Collectors.toList());
    }

    public static MazeResponseDTO fromMazeToMazeResponseDTO(Maze maze) {
        return MazeResponseDTO
                .builder()
                .id(maze.getId())
                .title(maze.getTitle())
                .description(maze.getDescription())
                .summary(maze.getSummary())
                .createdAt(maze.getCreatedAt())
                .visibility(maze.isVisibility())
                .image(maze.getImage())
                .difficulty(maze.getDifficulty())
                .author(fromProfileToMazeProfileDTO(maze.getAuthor()))
                .numberOfEnrolledUsers(maze.getEnrolledUsers().size())
                .numberOfSolvers(maze.getSolvers().size())
                .tags(maze.getTags())
                .pages(fromPageToMazePageDTO(maze.getPages()))
                .totalPoints(maze.getTotalPoints())
                .file(maze.getFile())
                .build();
    }

    public static List<MazeSimpleDTO> fromMazeToMazeSimpleDTO(List<Maze> mazes) {
        return mazes.stream().map(MazeMapper::fromMazeToMazeSimpleDTO).collect(Collectors.toList());
    }

    public static MazeSimpleDTO fromMazeToMazeSimpleDTO(Maze maze) {
        return MazeSimpleDTO
                .builder()
                .id(maze.getId())
                .title(maze.getTitle())
                .difficulty(maze.getDifficulty())
                .summary(maze.getSummary())
                .description(maze.getDescription())
                .image(maze.getImage())
                .author(fromProfileToMazeProfileDTO(maze.getAuthor()))
                .totalPoints(maze.getTotalPoints())
                .build();
    }

    public static List<LeaderboardMazeDTO> fromMazeToMazeToLeaderboardMazeDTO(List<Maze> mazes) {
        return mazes.stream().map(MazeMapper::fromMazeToMazeToLeaderboardMazeDTO).collect(Collectors.toList());
    }

    public static LeaderboardMazeDTO fromMazeToMazeToLeaderboardMazeDTO(Maze maze) {
        return LeaderboardMazeDTO
                .builder()
                .mazeId(maze.getId())
                .title(maze.getTitle())
                .image(maze.getImage())
                .difficulty(maze.getDifficulty())
                .build();
    }

    public static List<CreatedMazeDTO> fromMazeToCreatedMazeDTO(List<Maze> mazes) {
        return mazes.stream().map(MazeMapper::fromMazeToCreatedMazeDTO).toList();
    }

    public static CreatedMazeDTO fromMazeToCreatedMazeDTO(Maze maze) {
        return CreatedMazeDTO
                .builder()
                .id(maze.getId())
                .image(maze.getImage())
                .title(maze.getTitle())
                .build();
    }


}
