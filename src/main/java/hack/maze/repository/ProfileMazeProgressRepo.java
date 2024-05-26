package hack.maze.repository;

import hack.maze.entity.ProfileMazeProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileMazeProgressRepo extends JpaRepository<ProfileMazeProgress, Long> {
}
