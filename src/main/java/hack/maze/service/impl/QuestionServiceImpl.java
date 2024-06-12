package hack.maze.service.impl;

import hack.maze.constant.ApplicationConstant;
import hack.maze.dto.QuestionDTO;
import hack.maze.dto.QuestionResponseDTO;
import hack.maze.entity.Difficulty;
import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Question;
import hack.maze.repository.QuestionRepo;
import hack.maze.service.PageService;
import hack.maze.service.QuestionService;
import hack.maze.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.constant.ApplicationConstant.*;
import static hack.maze.mapper.QuestionMapper.fromQuestionToQuestionResponseDTO;
import static hack.maze.utils.GlobalMethods.checkUserAuthority;
import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepo questionRepo;
    private final PageService pageService;
    private final UserService userService;

    @Override
    @Transactional
    public Long createQuestion(long pageId, QuestionDTO questionDTO) {
        validateQuestionInfo(questionDTO);
        Page page = pageService._getSinglePage(pageId);
        checkPointLimitExceeds(questionDTO.points(), page.getMaze());
        Question savedQuestion = questionRepo.save(fillQuestionInfo(questionDTO, page));
        updateMazeTotalPoints(page.getMaze(), savedQuestion.getPoints());
        return savedQuestion.getId();
    }

    @Transactional
    protected void updateMazeTotalPoints(Maze maze, int questionPoints) {
        maze.setTotalPoints(maze.getTotalPoints() + questionPoints);
    }

    private void checkPointLimitExceeds(int points, Maze maze) {
        if ((points + maze.getTotalPoints()) > getMaxMazePoints(maze.getDifficulty())) {
            throw new RuntimeException("You have exceeded the maximum number of points");
        }
    }

    private Question fillQuestionInfo(QuestionDTO questionDTO, Page page) {
        return Question
                .builder()
                .type(questionDTO.type())
                .content(questionDTO.content())
                .answer(questionDTO.answer())
                .hint(questionDTO.hint())
                .page(page)
                .points(questionDTO.points())
                .build();
    }

    private int getMaxMazePoints(Difficulty mazeDifficulty) {
        if (mazeDifficulty == Difficulty.FUNDAMENTAL) {
            return FUNDAMENTAL_POINTS;
        }
        if (mazeDifficulty == Difficulty.EASY) {
            return EASY_POINTS;
        }
        if (mazeDifficulty == Difficulty.MEDIUM) {
            return MEDIUM_POINTS;
        }
        if (mazeDifficulty == Difficulty.HARD) {
            return HARD_POINTS;
        } else {
            throw new IllegalStateException("Please specify valid Difficulty value");
        }
    }

    private void validateQuestionInfo(QuestionDTO questionDTO) {
        Objects.requireNonNull(questionDTO.content(), nullMsg("content"));
        Objects.requireNonNull(questionDTO.answer(), nullMsg("answer"));
        Objects.requireNonNull(questionDTO.hint(), nullMsg("hint"));
        Objects.requireNonNull(questionDTO.type(), nullMsg("type"));
    }

    @Override
    public QuestionResponseDTO getSingleQuestion(long questionId) {
        return fromQuestionToQuestionResponseDTO(_getSingleQuestion(questionId));
    }

    @Override
    public Question _getSingleQuestion(long questionId) {
        return questionRepo.findById(questionId).orElseThrow(() -> new RuntimeException("Question with id = [" + questionId + "] not exist"));
    }

    @Override
    @Transactional
    public String updateQuestion(long questionId, QuestionDTO questionDTO) throws AccessDeniedException {
        Question targetQuestion = _getSingleQuestion(questionId);
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), targetQuestion);
        if (questionDTO.content() != null) {
            targetQuestion.setContent(questionDTO.content());
        }
        if (questionDTO.answer() != null) {
            targetQuestion.setAnswer(questionDTO.answer());
        }
        if (questionDTO.hint() != null) {
            targetQuestion.setHint(questionDTO.hint());
        }
        if (questionDTO.type() != null) {
            targetQuestion.setType(questionDTO.type());
        }
        return "Question with id = [" + questionId + "] updated successfully";
    }

    @Override
    public String deleteQuestion(long questionId) throws AccessDeniedException {
        Question targetQuestion = _getSingleQuestion(questionId);
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), targetQuestion);
        questionRepo.delete(targetQuestion);
        return "Question with id = [" + questionId + "] deleted successfully";
    }

    @Override
    public String getQuestionHint(long questionId) {
        Question question = _getSingleQuestion(questionId);
        return question.getHint();
    }

    @Override
    public String getQuestionAnswer(long questionId) throws AccessDeniedException {
        Question question = _getSingleQuestion(questionId);
        checkUserAuthority(userService.getSingleUser(getCurrentUser()), question);
        return question.getAnswer();
    }
}
