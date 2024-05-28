package hack.maze.repository;

import hack.maze.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageRepo extends JpaRepository<Page, Long> {
    @Query("SELECT p FROM Page p WHERE p.maze.id = ?1")
    List<Page> getAllPagesInSpecificMaze(long mazeId);
}
