package hack.maze.repository;

import hack.maze.entity.Maze;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MazeRepo extends JpaRepository<Maze, Long> {
    Optional<Maze> findByTitle(String title);
}
