package hack.maze.repository;

import hack.maze.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepo extends JpaRepository<UserProgress, Long> {
}
