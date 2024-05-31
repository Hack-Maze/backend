package hack.maze.service.impl;

import hack.maze.dto.QuestionDTO;
import hack.maze.entity.Page;
import hack.maze.entity.Question;
import hack.maze.repository.QuestionRepo;
import hack.maze.service.PageService;
import hack.maze.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static hack.maze.config.UserContext.getCurrentUser;
import static hack.maze.utils.GlobalMethods.checkUserAuthority;
import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepo questionRepo;
    private final PageService pageService;

    @Override
    public String createQuestion(long pageId, QuestionDTO questionDTO) {
        validateQuestionInfo(questionDTO);
        Page page = pageService.getSinglePage(pageId);
        questionRepo.save(fillQuestionInfo(questionDTO, page));
        return "Question created Successfully";
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
    public Question getSingleQuestion(long questionId) {
        return questionRepo.findById(questionId).orElseThrow(() -> new RuntimeException("Question with id = [" + questionId + "] not exist"));
    }

    @Override
    @Transactional
    public String updateQuestion(long questionId, QuestionDTO questionDTO) throws AccessDeniedException {
        Question targetQuestion = getSingleQuestion(questionId);
        checkUserAuthority(getCurrentUser(), targetQuestion);
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
        Question targetQuestion = getSingleQuestion(questionId);
        checkUserAuthority(getCurrentUser(), targetQuestion);
        questionRepo.delete(targetQuestion);
        return "Question with id = [" + questionId + "] deleted successfully";
    }
}
