package hack.maze.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePageProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;

    @ManyToOne
    @JoinColumn(name = "profile_maze_progress_id", referencedColumnName = "id")
    private ProfileMazeProgress profileMazeProgress;

    @OneToMany(mappedBy = "profilePageProgress", cascade = CascadeType.REMOVE)
    private List<QuestionProgress> questionProgresses;

    private boolean isCompleted;
    private LocalDateTime completedAt;

}
