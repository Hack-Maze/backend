package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore // TODO: remove json ignore and replace it with dto
    private AppUser appUser;
    private String fullName;
    private String country;
    private String image;
    private double rank;
    private String bio;
    private String githubLink;
    private String linkedinLink;
    private String personalWebsite;
    private String job;
    private LocalDateTime lastQuestionSolvedAt;

    @OneToMany(mappedBy = "profile")
    @JsonManagedReference
    private List<ProfileMazeProgress> profileMazeProgresses;

    @OneToMany(mappedBy = "profile")
    @JsonManagedReference
    private List<ProfilePageProgress> profilePageProgresses;

    @OneToMany(mappedBy = "author")
    @JsonIgnore // TODO: remove json ignore and replace it with dto
    private List<Maze> createdMazes;

    @ManyToOne
    @JoinColumn(name = "last_activate_maze_id", referencedColumnName = "id")
    private Maze lastActiveMaze;

    @ManyToMany(mappedBy = "enrolledUsers")
    @JsonBackReference
    private List<Maze> enrolledMazes;


    @ManyToMany
    @JoinTable(
            name = "profile_badge",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id"))
    @JsonManagedReference
    private List<Badge> badges;

}
