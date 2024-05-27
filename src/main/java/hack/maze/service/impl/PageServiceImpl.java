package hack.maze.service.impl;

import hack.maze.dto.PageRequestDTO;
import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.repository.PageRepo;
import hack.maze.service.MazeService;
import hack.maze.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@Slf4j
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepo pageRepo;
    private final MazeService mazeService;

    @Override
    public String createPage(long mazeId, PageRequestDTO pageRequestDTO) {
        validatePageInfo(pageRequestDTO);
        Maze maze = mazeService._getSingleMaze(mazeId);
        pageRepo.save(fillPageInfo(pageRequestDTO, maze));
        return "page with title = [" + pageRequestDTO.title() + "] added successfully to maze with title = [" + maze.getTitle() + "]";
    }

    private Page fillPageInfo(PageRequestDTO pageRequestDTO, Maze maze) {
        return Page
                .builder()
                .title(pageRequestDTO.title())
                .description(pageRequestDTO.description())
                .content(pageRequestDTO.content())
                .maze(maze)
                .build();
    }

    private void validatePageInfo(PageRequestDTO page) {
        Objects.requireNonNull(page.title(), nullMsg("title"));
        Objects.requireNonNull(page.description(), nullMsg("description"));
        Objects.requireNonNull(page.content(), nullMsg("content"));
    }

    @Override
    public List<Page> getAllPagesInSpecificMaze(long mazeId) {
        return pageRepo.getAllPagesInSpecificMaze(mazeId);
    }

    @Override
    public Page getSinglePage(long pageId) {
        return pageRepo.findById(pageId).orElseThrow(() -> new RuntimeException("page with id = [" + pageId + "] not exist"));
    }

    @Override
    @Transactional
    public String updatePage(long pageId, PageRequestDTO pageRequestDTO) {
        Page taragetPage = getSinglePage(pageId);
        if (pageRequestDTO.title() != null) {
            taragetPage.setTitle(pageRequestDTO.title());
        }
        if (pageRequestDTO.content() != null) {
            taragetPage.setContent(pageRequestDTO.content());
        }
        if (pageRequestDTO.description() != null) {
            taragetPage.setDescription(pageRequestDTO.description());
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
