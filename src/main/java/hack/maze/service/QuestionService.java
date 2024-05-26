package hack.maze.service;

import hack.maze.entity.Question;

import java.util.List;

public interface QuestionService {
    String createQuestion(long pageId, Question question);
    List<Question> getAllQuestions();
    Question getSingleQuestion(long questionId);
    String updateQuestion(long questionId, Question question);
    String deleteQuestion(long questionId);
}
