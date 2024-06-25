package hack.maze.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private QuestionType type;
    private String content;
    private String answer;
    private String hint;
    private int points;
    private String envKey;

    @ManyToOne
    @JsonBackReference
    private Page page;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<QuestionProgress> questionProgresses;
}
