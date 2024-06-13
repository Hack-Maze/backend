package hack.maze.mapper;

import hack.maze.dto.MazeProgressDTO;
import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
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



    public static MazeProgressDTO fromMazeToMazeProgressDTO(Maze maze) {
        return MazeProgressDTO
                .builder()
                .id(maze.getId())
                .title(maze.getTitle())
                .image(maze.getImage())
                .build();
    }

}
