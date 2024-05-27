package hack.maze.repository;

import hack.maze.entity.ProfileMazeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileMazeProgressRepo extends JpaRepository<ProfileMazeProgress, Long> {
    @Query("SELECT pmp FROM ProfileMazeProgress pmp WHERE pmp.profile.id = ?1 AND pmp.maze.id = ?2")
    Optional<ProfileMazeProgress> findByProfileIdAndMazeId(long profileId, long mazeId);
}
