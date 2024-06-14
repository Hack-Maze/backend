package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne
    private ProfileMazeProgress mazeProgress;

    @OneToMany(mappedBy = "profilePageProgress", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ProfileQuestionProgress> profileQuestionProgresses;

    private long numberOfSolvedQuestions;

    private boolean isCompleted;
}
