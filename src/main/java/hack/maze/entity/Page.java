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

    @Column(columnDefinition = "text")
    @Basic(fetch = FetchType.LAZY)
    private String description;

    @Column(columnDefinition = "text")
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @OneToMany(mappedBy = "page", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Question> questions;

    @ManyToOne
    @JsonBackReference
    private Maze maze;
}
