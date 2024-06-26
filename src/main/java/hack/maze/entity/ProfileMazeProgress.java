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
public class ProfileMazeProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "maze_id", referencedColumnName = "id")
    private Maze maze;

    @OneToMany(mappedBy = "profileMazeProgress", cascade = CascadeType.REMOVE)
    private List<ProfilePageProgress> profilePageProgresses;


    private LocalDateTime enrolledAt;
    private boolean isCompleted;

}
