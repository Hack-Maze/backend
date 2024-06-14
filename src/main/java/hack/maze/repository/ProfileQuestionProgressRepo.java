package hack.maze.repository;

import hack.maze.entity.ProfileQuestionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProfileQuestionProgressRepo extends JpaRepository<ProfileQuestionProgress, Long> {
    @Query("SELECT pqp FROM ProfileQuestionProgress pqp WHERE DATE(pqp.solvedAt) BETWEEN ?1 AND ?2")
    List<ProfileQuestionProgress> findBySolvedAtBetween(LocalDate start, LocalDate end);
}
