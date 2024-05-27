package hack.maze.service.impl;

import hack.maze.config.UserContext;
import hack.maze.constant.ApplicationConstant;
import hack.maze.entity.*;
import hack.maze.repository.PageRepo;
import hack.maze.repository.ProfileQuestionProgressRepo;
import hack.maze.service.MazeService;
import hack.maze.service.PageService;
import hack.maze.service.ProfilePageProgressService;
import hack.maze.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static hack.maze.constant.ApplicationConstant.SOLVE_POINTS;
import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@Slf4j
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepo pageRepo;
    private final MazeService mazeService;
    private final ProfilePageProgressService profilePageProgressService;
    private final QuestionService questionService;
    private final ProfileQuestionProgressRepo profileQuestionProgressRepo;

    @Override
    public String createPage(long mazeId, Page page) {
        validatePageInfo(page);
        Maze maze = mazeService._getSingleMaze(mazeId);
        page.setMaze(maze);
        pageRepo.save(page);
        return "page with title = [" + page.getTitle() + "] added successfully to maze with title = [" + maze.getTitle() + "]";
    }

    private void validatePageInfo(Page page) {
        Objects.requireNonNull(page.getTitle(), nullMsg("title"));
        Objects.requireNonNull(page.getDescription(), nullMsg("description"));
        Objects.requireNonNull(page.getContent(), nullMsg("content"));
    }

    @Override
    public List<Page> getAllPages() {
        return pageRepo.findAll();
    }

    @Override
    public Page getSinglePage(long pageId) {
        return pageRepo.findById(pageId).orElseThrow(() -> new RuntimeException("page with id = [" + pageId + "] not exist"));
    }

    @Override
    @Transactional
    public String updatePage(long pageId, Page page) {
        Page taragetPage = getSinglePage(pageId);
        if (page.getTitle() != null) {
            taragetPage.setTitle(page.getTitle());
        }
        if (page.getContent() != null) {
            taragetPage.setContent(page.getContent());
        }
        if (page.getDescription() != null) {
            taragetPage.setDescription(page.getDescription());
        }

        return "Page with id = [" + pageId + "] updated successfully";
    }

    @Override
    public String deletePage(long pageId) {
        Page taragetPage = getSinglePage(pageId);
        pageRepo.delete(taragetPage);
        return "Page with title = [" + taragetPage.getTitle() + "] deleted successfully";
    }

}
