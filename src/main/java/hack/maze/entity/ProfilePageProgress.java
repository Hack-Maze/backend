package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonBackReference
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private Page page;

    @OneToMany(mappedBy = "profilePageProgress", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ProfileQuestionProgress> profileQuestionProgresses;

    private long totalNumberOfQuestions;
    private long numberOfSolvedQuestions;

    private boolean isCompleted;
}
