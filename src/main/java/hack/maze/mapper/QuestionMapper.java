package hack.maze.mapper;

import hack.maze.dto.QuestionProgressDTO;
import hack.maze.dto.QuestionResponseDTO;
import hack.maze.entity.Question;
import hack.maze.entity.QuestionProgress;

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

    public static List<QuestionProgressDTO> fromQuestionToQuestionProgressDTO(List<QuestionProgress> questionProgresses) {
        return questionProgresses.stream().map(QuestionMapper::fromQuestionToQuestionProgressDTO).collect(Collectors.toList());
    }
    public static QuestionProgressDTO fromQuestionToQuestionProgressDTO(QuestionProgress questionProgress) {
        return QuestionProgressDTO
                .builder()
                .id(questionProgress.getQuestion().getId())
                .solvedAt(questionProgress.getSolvedAt())
                .answer(questionProgress.getQuestion().getAnswer())
                .build();
    }

}
