package hack.maze.service.impl;

import hack.maze.entity.Page;
import hack.maze.entity.Question;
import hack.maze.repository.QuestionRepo;
import hack.maze.service.PageService;
import hack.maze.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepo questionRepo;
    private final PageService pageService;

    @Override
    public String createQuestion(long pageId, Question question) {
        validateQuestionInfo(question);
        Page page = pageService.getSinglePage(pageId);
        question.setPage(page);
        return "Question created Successfully";
    }

    private void validateQuestionInfo(Question question) {
        Objects.requireNonNull(question.getContent());
        Objects.requireNonNull(question.getAnswer());
        Objects.requireNonNull(question.getHint());
        Objects.requireNonNull(question.getType());
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    @Override
    public Question getSingleQuestion(long questionId) {
        return questionRepo.findById(questionId).orElseThrow(() -> new RuntimeException("Question with id = [" + questionId + "] not exist"));
    }

    @Override
    @Transactional
    public String updateQuestion(long questionId, Question question) {
        Question targetQuestion = getSingleQuestion(questionId);
        if (question.getContent() != null) {
            targetQuestion.setContent(question.getContent());
        }
        if (question.getAnswer() != null) {
            targetQuestion.setAnswer(question.getAnswer());
        }
        if (question.getHint() != null) {
            targetQuestion.setHint(question.getHint());
        }
        if (question.getType() != null) {
            targetQuestion.setType(question.getType());
        }
        return "Question with id = [" + questionId + "] updated successfully";
    }

    @Override
    public String deleteQuestion(long questionId) {
        Question targetQuestion = getSingleQuestion(questionId);
        questionRepo.delete(targetQuestion);
        return "Question with id = [" + questionId + "] deleted successfully";
    }
}
