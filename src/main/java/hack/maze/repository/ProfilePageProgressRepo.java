package hack.maze.repository;


import hack.maze.entity.ProfilePageProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfilePageProgressRepo extends JpaRepository<ProfilePageProgress, Long> {
    @Query("SELECT ppp FROM ProfilePageProgress ppp WHERE ppp.profile.id = ?1 AND ppp.page.id = ?2")
    Optional<ProfilePageProgress> findByProfileIdAndPageId(long profileId, long pageId);

    @Query("SELECT ppp FROM ProfilePageProgress ppp WHERE ppp.profile.id = ?1")
    List<ProfilePageProgress> getProfilePagesProgressByProfileId(long pageOId);
}
