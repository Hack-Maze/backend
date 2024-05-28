package hack.maze.service;

import hack.maze.dto.PageRequestDTO;
import hack.maze.entity.Page;

import java.util.List;

public interface PageService {
    String createPage(long mazeId, PageRequestDTO pageRequestDTO);
    List<Page> getAllPagesInSpecificMaze(long mazeId);
    Page getSinglePage(long pageId);
    String updatePage(long pageId, PageRequestDTO pageRequestDTO);
    String deletePage(long pageId);
}