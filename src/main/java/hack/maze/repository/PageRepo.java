package hack.maze.repository;

import hack.maze.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepo extends JpaRepository<Page, Long> {
}
