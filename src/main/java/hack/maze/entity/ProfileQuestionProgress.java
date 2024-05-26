package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileQuestionProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    private LocalDateTime solvedAt;

    @ManyToOne
    @JoinColumn(name = "profile_page_progress_id", referencedColumnName = "id")
    private ProfilePageProgress profilePageProgress;

    private boolean isCompleted;
}
