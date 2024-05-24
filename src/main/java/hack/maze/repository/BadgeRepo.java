package hack.maze.repository;

import hack.maze.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepo extends JpaRepository<Badge, Long> {
}
