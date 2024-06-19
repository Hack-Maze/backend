package hack.maze.repository;

import hack.maze.entity.AzureContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AzureContainerRepo extends JpaRepository<AzureContainer, Long> {

    @Query("SELECT ac FROM AzureContainer ac WHERE ac.maze.id = ?1")
    Optional<AzureContainer> findByMazeId(Long maze);

}
