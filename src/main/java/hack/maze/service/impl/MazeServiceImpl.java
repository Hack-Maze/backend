package hack.maze.service.impl;

import hack.maze.dto.CreateMazeDTO;
import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.entity.AppUser;
import hack.maze.entity.Maze;
import hack.maze.entity.Tag;
import hack.maze.repository.MazeRepo;
import hack.maze.service.MazeService;
import hack.maze.service.TagService;
import hack.maze.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUsername;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeResponseDTO;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeSimpleDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class MazeServiceImpl implements MazeService {

    private final MazeRepo mazeRepo;
    private final TagService tagService;

    @Override
    public String createMaze(CreateMazeDTO createMazeDTO) {
        validateCreateMazeDTO(createMazeDTO);
        mazeRepo.save(fillMazeInfo(createMazeDTO));
        return "new maze created Successfully";
    }

    private Maze fillMazeInfo(CreateMazeDTO createMazeDTO) {
        return Maze
                .builder()
                .tags(getTagsFromListOfTagIds(createMazeDTO.tagIds()))
                .image(sendImageToAzure(createMazeDTO.image()))
                .visibility(true)
                .title(createMazeDTO.title())
                .description(createMazeDTO.description())
                .summary(createMazeDTO.summary())
                .build();

    }

    private String sendImageToAzure(MultipartFile image) {
        return "image";
    }

    private List<Tag> getTagsFromListOfTagIds(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();
        for (long tagId : tagIds) {
            tags.add(tagService.getSingleTag(tagId));
        }
        return tags;
    }

    private void validateCreateMazeDTO(CreateMazeDTO createMazeDTO) {
        Objects.requireNonNull(createMazeDTO.title());
        Objects.requireNonNull(createMazeDTO.description());
        Objects.requireNonNull(createMazeDTO.summary());
        Objects.requireNonNull(createMazeDTO.tagIds());
        Objects.requireNonNull(createMazeDTO.image());
    }

    @Override
    public List<MazeSimpleDTO> getAllMazes() {
        return fromMazeToMazeSimpleDTO(mazeRepo.findAll());
    }

    @Override
    public MazeResponseDTO getSingleMaze(long mazeId) {
        return fromMazeToMazeResponseDTO(_getSingleMaze(mazeId));
    }

    @Override
    public Maze _getSingleMaze(long mazeId) {
        return mazeRepo.findById(mazeId).orElseThrow(() -> new RuntimeException("maze with id = [" + mazeId + "] not exist"));
    }

    @Override
    public String deleteMaze(long mazeId) throws AccessDeniedException {
        Maze maze = _getSingleMaze(mazeId);
        checkUserAuthority(getCurrentUsername(), maze);
        mazeRepo.deleteById(maze.getId());
        log.warn("maze with id = [{}] will be deleted completely", mazeId);
        return "Maze with id = [" + mazeId + "] deleted successfully";
    }

    private void checkUserAuthority(String currentUsername, Maze targetMaze) throws AccessDeniedException {
        if (!Objects.equals(currentUsername, targetMaze.getAuthor().getAppUser().getEmail())) {
            throw new AccessDeniedException("Access denied!");
        }
    }
}
