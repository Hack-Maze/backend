package hack.maze.repository;

import hack.maze.dto.LeaderboardMazeDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MazeRepo extends JpaRepository<Maze, Long> {
    Optional<Maze> findByTitle(String title);

    @Query("SELECT m FROM Maze m JOIN m.solvers s WHERE s.id = ?1")
    List<Maze> getSolvedMazesByProfileId(long profileId);


}
