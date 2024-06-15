package hack.maze.repository;

import hack.maze.entity.QuestionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuestionProgressRepo extends JpaRepository<QuestionProgress, Long> {
    @Query("SELECT qp FROM QuestionProgress qp WHERE qp.profile.id = ?1 AND qp.question.id = ?2")
    Optional<QuestionProgress> findByProfileIdAndQuestionId(Long profileId, Long questionId);

    @Query("SELECT qp FROM QuestionProgress qp WHERE qp.profile.id = ?1 AND qp.profilePageProgress.page.id = ?2")
    List<QuestionProgress> findByProfileIdAndPageId(Long profileId, Long pageId);

    @Query("SELECT qp FROM QuestionProgress qp WHERE DATE(qp.solvedAt) BETWEEN ?1 AND ?2")
    List<QuestionProgress> findBySolvedAtBetween(LocalDate start, LocalDate end);
}
