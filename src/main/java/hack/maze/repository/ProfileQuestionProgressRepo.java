package hack.maze.repository;

import hack.maze.entity.ProfileQuestionProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileQuestionProgressRepo extends JpaRepository<ProfileQuestionProgress, Long> {
}
