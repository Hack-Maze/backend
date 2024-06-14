package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonBackReference
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "maze_id")
    private Maze maze;

    @OneToMany(mappedBy = "mazeProgress")
    List<ProfilePageProgress> profilePageProgresses;

    private boolean isCompleted;
}
