package hack.maze.entity;

import com.fasterxml.jackson.annotation.*;
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
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String content;

    @OneToMany(mappedBy = "page", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Question> questions;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "page")
    @JsonIgnore
    private List<ProfilePageProgress> profilePageProgresses;

    @ManyToOne
    @JsonBackReference
    private Maze maze;
}
