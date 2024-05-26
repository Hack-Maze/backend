package hack.maze.repository;

import hack.maze.entity.Maze;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MazeRepo extends JpaRepository<Maze, Long> {

    @Query("""
        SELECT
        m.id AS mazeId, COUNT(q.id) AS questionsCount
        FROM
        Maze m
        JOIN m.pages p
        JOIN p.questions q
        WHERE m.id = ?1
        GROUP BY m.id
    """)
    long findMazeQuestionsCount(Long mazeId);
}
