package hack.maze.entity;

import com.fasterxml.jackson.annotation.*;
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
public class Maze {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String summary;
    private LocalDateTime createdAt;
    private boolean visibility;
    private String image;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToMany
    @JoinTable(
            name = "maze_profile_enrollment",
            joinColumns = @JoinColumn(name = "maze_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    @JsonIgnore
    private List<Profile> enrolledUsers;

    @ManyToMany
    @JoinTable(
            name = "maze_profile_solvers",
            joinColumns = @JoinColumn(name = "maze_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    @JsonIgnore
    private List<Profile> solvers;

    @ManyToMany
    @JoinTable(
            name = "maze_tag",
            joinColumns = @JoinColumn(name = "maze_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonManagedReference
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @JsonManagedReference
    private Profile author;

    @OneToMany(mappedBy = "maze", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Page> pages;

    @OneToMany(mappedBy = "maze", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ProfileMazeProgress> profileMazeProgresses;


}
