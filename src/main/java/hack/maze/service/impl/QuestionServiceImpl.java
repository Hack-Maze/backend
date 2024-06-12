package hack.maze.service.impl;

import hack.maze.dto.QuestionDTO;
import hack.maze.dto.QuestionResponseDTO;
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
    public Long createQuestion(long pageId, QuestionDTO questionDTO) {
        validateQuestionInfo(questionDTO);
        Page page = pageService._getSinglePage(pageId);
        Question savedQuestion = questionRepo.save(fillQuestionInfo(questionDTO, page));
        return savedQuestion.getId();
    }

    private Question fillQuestionInfo(QuestionDTO questionDTO, Page page) {
        return Question
                .builder()
                .type(questionDTO.type())
                .content(questionDTO.content())
                .answer(questionDTO.answer())
                .hint(questionDTO.hint())
                .page(page)
                .build();
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
