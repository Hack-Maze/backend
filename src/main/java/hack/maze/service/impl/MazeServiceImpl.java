package hack.maze.service.impl;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.constant.AzureConstant.IMAGES_BLOB_CONTAINER_MAZES;
import hack.maze.dto.LeaderboardMazeDTO;
import hack.maze.dto.MazeResponseDTO;
import hack.maze.dto.MazeSimpleDTO;
import hack.maze.dto.UpdateMazeDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Profile;
import hack.maze.entity.Tag;
import hack.maze.entity.Type;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeResponseDTO;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeSimpleDTO;
import static hack.maze.mapper.MazeMapper.fromMazeToMazeToLeaderboardMazeDTO;
import hack.maze.repository.MazeRepo;
import hack.maze.service.AzureService;
import hack.maze.service.MazeService;
import hack.maze.service.ProfileService;
import hack.maze.service.TagService;
import hack.maze.service.UserService;
import static hack.maze.utils.GlobalMethods.checkUserAuthority;
import static hack.maze.utils.GlobalMethods.nullMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MazeServiceImpl implements MazeService {

    private final MazeRepo mazeRepo;
    private final TagService tagService;
    private final UserService userService;
    private final AzureService azureService;
    private final ProfileService profileService;

    @Override
    @Transactional
    public Long createMaze(UpdateMazeDTO updateMazeDTO) throws IOException {
        validateUpdateMazeDTO(updateMazeDTO);
        checkMazeExistence(updateMazeDTO.title());
        Maze savedMaze = mazeRepo.save(fillMazeInfo(updateMazeDTO));
        if (updateMazeDTO.image() != null) {
            savedMaze.setImage(azureService.sendImageToAzure(updateMazeDTO.image(), IMAGES_BLOB_CONTAINER_MAZES, savedMaze.getId()));
        }
        if (updateMazeDTO.file() != null) {
            savedMaze.setType(Type.valueOf(updateMazeDTO.type()));
            savedMaze.setFile(azureService.sendImageToAzure(updateMazeDTO.file(), IMAGES_BLOB_CONTAINER_MAZES, savedMaze, savedMaze.getType()));
            buildImageFromMaze(savedMaze);
        }
        mazeRepo.save(savedMaze);
        updateProfileCreatedMazes(savedMaze);
        return savedMaze.getId();
    }

    @Transactional
    protected void updateProfileCreatedMazes(Maze savedMaze) {
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        List<Maze> createdMazes = profile.getCreatedMazes();
        createdMazes.add(savedMaze);
        profile.setCreatedMazes(createdMazes);
    }

    private void checkMazeExistence(String title) {
        if (mazeRepo.findByTitle(title).isPresent()) {
            throw new RuntimeException("Maze with title = [" + title + "] already exist");
        }
    }

    private Maze fillMazeInfo(UpdateMazeDTO updateMazeDTO) {
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
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), maze);
        azureService.removeImageFromAzure(IMAGES_BLOB_CONTAINER_MAZES, maze.getId().toString() + "/");
        mazeRepo.deleteById(maze.getId());
        log.warn("maze with id = [{}] will be deleted completely", mazeId);
        return "Maze with id = [" + mazeId + "] deleted successfully";
    }

    @Override
    @Transactional
    public String updateMaze(long mazeId, UpdateMazeDTO updateMazeDTO) throws IOException {
        Maze maze = _getSingleMaze(mazeId);
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), maze);
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
        if (updateMazeDTO.file() != null) {
            maze.setImage(azureService.sendImageToAzure(updateMazeDTO.file(), IMAGES_BLOB_CONTAINER_MAZES, maze, Type.valueOf(updateMazeDTO.type())));
            buildImageFromMaze(maze);
        }
        return "maze with id = [" + mazeId + "] updated successfully";
    }

    @Override
    public boolean isCurrentUserEnrolledInMaze(long mazeId) {
        Maze maze = _getSingleMaze(mazeId);
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        return maze.getEnrolledUsers().contains(profile);
    }

    @Override
    public boolean isCurrentUserSolverInMaze(long mazeId) {
        Maze maze = _getSingleMaze(mazeId);
        Profile profile = profileService._getSingleProfile(getCurrentUser());
        return maze.getSolvers().contains(profile);
    }



    @Override
    @Transactional
    public String buildImageFromMaze(long mazeId) {
        Maze maze = _getSingleMaze(mazeId);
        return azureService.runImageBuildWorkFlow(maze);
    }

    @Override
    @Transactional
    public void buildImageFromMaze(Maze maze) {
        azureService.runImageBuildWorkFlow(maze);
    }

    @Override
    public String runContainer(long mazeId) {
        Maze maze = _getSingleMaze(mazeId);
        azureService.runYourContainer(maze);
        return "container created successfully";
    }

    @Override
    public List<LeaderboardMazeDTO> getSolvedMazesByProfileId() {
        return fromMazeToMazeToLeaderboardMazeDTO(mazeRepo.getSolvedMazesByProfileId(profileService._getSingleProfile(getCurrentUser()).getId()));
    }

    @Override
    public List<LeaderboardMazeDTO> getSolvedMazesByProfileId(long profileId) {
        return fromMazeToMazeToLeaderboardMazeDTO(mazeRepo.getSolvedMazesByProfileId(profileId));
    }


}
