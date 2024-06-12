package hack.maze.mapper;

import hack.maze.dto.QuestionResponseDTO;
import hack.maze.entity.Question;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionMapper {

    public static List<QuestionResponseDTO> fromQuestionToQuestionResponseDTO(List<Question> questions) {
        return questions.stream().map(QuestionMapper::fromQuestionToQuestionResponseDTO).collect(Collectors.toList());
    }

    public static QuestionResponseDTO fromQuestionToQuestionResponseDTO(Question question) {
        return QuestionResponseDTO
                .builder()
                .id(question.getId())
                .type(question.getType())
                .content(question.getContent())
                .points(question.getPoints())
                .build();
    }
}
