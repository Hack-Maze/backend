package hack.maze.service.impl;

import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.UpdateMazeDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Tag;
import hack.maze.repository.MazeRepo;
import hack.maze.service.AzureService;
import hack.maze.service.MazeService;
import hack.maze.service.ProfileService;
import hack.maze.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.constant.AzureConstant.IMAGES_BLOB_CONTAINER_MAZES;
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
    private final ProfileService profileService;

    @Override
//    @Transactional
    public Long createMaze(UpdateMazeDTO updateMazeDTO) throws IOException {
        validateUpdateMazeDTO(updateMazeDTO);
        Maze savedMaze = mazeRepo.save(fillMazeInfo(updateMazeDTO));
        savedMaze.setImage(azureService.sendImageToAzure(updateMazeDTO.image(), IMAGES_BLOB_CONTAINER_MAZES, savedMaze.getId()));
        mazeRepo.save(savedMaze);
        return savedMaze.getId();
    }

    private Maze fillMazeInfo(UpdateMazeDTO updateMazeDTO) throws IOException {
        return Maze
                .builder()
                .visibility(true)
                .title(updateMazeDTO.title())
                .description(updateMazeDTO.description())
                .summary(updateMazeDTO.summary())
                .author(profileService._getSingleProfile(getCurrentUser()))
                .createdAt(LocalDateTime.now())
                .difficulty(updateMazeDTO.difficulty())
                .build();
    }

    private List<Tag> getTagsFromListOfTagIds(List<Long> tagIds) {
        List<Tag> tags = new ArrayList<>();
        for (long tagId : tagIds) {
            tags.add(tagService.getSingleTag(tagId));
        }
        return tags;
    }

    private void validateUpdateMazeDTO(UpdateMazeDTO updateMazeDTO) {
        Objects.requireNonNull(updateMazeDTO.title(), nullMsg("title"));
        Objects.requireNonNull(updateMazeDTO.description(), nullMsg("description"));
        Objects.requireNonNull(updateMazeDTO.summary(), nullMsg("summary"));
        Objects.requireNonNull(updateMazeDTO.difficulty(), nullMsg("difficulty"));
//        Objects.requireNonNull(updateMazeDTO.image(), nullMsg("image"));
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
    public String updateMaze(long mazeId, UpdateMazeDTO updateMazeDTO) throws IOException {
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
            maze.setImage(azureService.sendImageToAzure(updateMazeDTO.image(), IMAGES_BLOB_CONTAINER_MAZES, maze.getId()));
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
