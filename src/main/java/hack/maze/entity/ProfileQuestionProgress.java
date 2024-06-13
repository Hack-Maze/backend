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
@Table(indexes = {@Index(name = "sa_idx", columnList = "solvedAt")})
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
    @JsonBackReference
    private ProfilePageProgress profilePageProgress;
}
