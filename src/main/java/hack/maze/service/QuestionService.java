package hack.maze.service;

import hack.maze.dto.QuestionDTO;
import hack.maze.entity.Question;

import java.util.List;

public interface QuestionService {
    String createQuestion(long pageId, QuestionDTO questionDTO);
    Question getSingleQuestion(long questionId);
    String updateQuestion(long questionId, QuestionDTO questionDTO);
    String deleteQuestion(long questionId);
}
