package hack.maze.repository;

import hack.maze.entity.ProfileMaze;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileMazeRepo extends JpaRepository<ProfileMaze, Long> {
}
