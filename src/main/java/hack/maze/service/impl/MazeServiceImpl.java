package hack.maze.service.impl;

import hack.maze.config.UserContext;
import hack.maze.dto.CreateMazeDTO;
import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.UpdateMazeDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Profile;
import hack.maze.entity.ProfileMazeProgress;
import hack.maze.entity.Tag;
import hack.maze.repository.MazeRepo;
import hack.maze.service.AzureService;
import hack.maze.service.MazeService;
import hack.maze.service.ProfileMazeProgressService;
import hack.maze.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeResponseDTO;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeSimpleDTO;
import static hack.maze.utils.GlobalMethods.checkUserAuthority;
import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@RequiredArgsConstructor
@Slf4j
public class MazeServiceImpl implements MazeService {

    private final MazeRepo mazeRepo;
    private final TagService tagService;
    private final AzureService azureService;
    private final ProfileMazeProgressService profileMazeProgressService;

    @Override
    public String createMaze(CreateMazeDTO createMazeDTO) {
        validateCreateMazeDTO(createMazeDTO);
        mazeRepo.save(fillMazeInfo(createMazeDTO));
        return "new maze created Successfully";
    }

    private Maze fillMazeInfo(CreateMazeDTO createMazeDTO) {
        return Maze
                .builder()
                .visibility(true)
                .title(createMazeDTO.title())
                .description(createMazeDTO.description())
                .summary(createMazeDTO.summary())
                .author(getCurrentUser().getProfile())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<Tag> getTagsFromListOfTagIds(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();
        for (long tagId : tagIds) {
            tags.add(tagService.getSingleTag(tagId));
        }
        return tags;
    }

    private void validateCreateMazeDTO(CreateMazeDTO createMazeDTO) {
        Objects.requireNonNull(createMazeDTO.title(), nullMsg("title"));
        Objects.requireNonNull(createMazeDTO.description(), nullMsg("description"));
        Objects.requireNonNull(createMazeDTO.summary(), nullMsg("summary"));
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
        checkUserAuthority(getCurrentUser(), maze);
        mazeRepo.deleteById(maze.getId());
        log.warn("maze with id = [{}] will be deleted completely", mazeId);
        return "Maze with id = [" + mazeId + "] deleted successfully";
    }

    @Override
    @Transactional
    public String updateMaze(long mazeId, UpdateMazeDTO updateMazeDTO) throws AccessDeniedException {
        Maze maze = _getSingleMaze(mazeId);
        checkUserAuthority(getCurrentUser(), maze);
        if (updateMazeDTO.title() != null) {
            maze.setTitle(updateMazeDTO.title());
        }
        if (updateMazeDTO.summary() != null) {
            maze.setSummary(updateMazeDTO.summary());
        }
        if (updateMazeDTO.description() != null) {
            maze.setDescription(updateMazeDTO.description());
        }
        if (updateMazeDTO.tagIds() != null) {
            maze.setTags(getTagsFromListOfTagIds(updateMazeDTO.tagIds()));
        }
        if (updateMazeDTO.image() != null) {
            maze.setImage(azureService.sendImageToAzure(updateMazeDTO.image()));
        }
        if (updateMazeDTO.difficulty() != null) {
            maze.setDifficulty(updateMazeDTO.difficulty());
        }
        if (updateMazeDTO.visibility() != null) {
            maze.setVisibility(updateMazeDTO.visibility());
        }
        return "maze with id = [" + mazeId + "] updated successfully";
    }


}
