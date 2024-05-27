package hack.maze.repository;

import hack.maze.entity.Maze;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MazeRepo extends JpaRepository<Maze, Long> {
}
