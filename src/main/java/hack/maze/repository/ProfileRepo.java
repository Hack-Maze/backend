package hack.maze.repository;

import hack.maze.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepo extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p where p.appUser.username = ?1")
    Optional<Profile> findByUsername(String username);

    @Query("SELECT p FROM Profile p where p.appUser.id = ?1")
    Optional<Profile> findByUserId(Long id);

}
