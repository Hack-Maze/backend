package hack.maze.service;

import hack.maze.dto.QuestionDTO;
import hack.maze.dto.QuestionResponseDTO;
import hack.maze.entity.Question;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface QuestionService {
    Long createQuestion(long pageId, QuestionDTO questionDTO);
    QuestionResponseDTO getSingleQuestion(long questionId);
    Question _getSingleQuestion(long questionId);
    String updateQuestion(long questionId, QuestionDTO questionDTO) throws AccessDeniedException;
    String deleteQuestion(long questionId) throws AccessDeniedException;
    String getQuestionHint(long questionId);
    String getQuestionAnswer(long questionId) throws AccessDeniedException;
    void assignEnvToQuestion(long questionId, String envKey);
}
