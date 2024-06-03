package hack.maze.service;

import hack.maze.dto.PageRequestDTO;
import hack.maze.dto.PageResponseDTO;
import hack.maze.entity.Page;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PageService {
    Long createPage(long mazeId, PageRequestDTO pageRequestDTO);
    List<PageResponseDTO> getAllPagesInSpecificMaze(long mazeId);
    PageResponseDTO getSinglePage(long pageId);
    Page _getSinglePage(long pageId);
    String updatePage(long pageId, PageRequestDTO pageRequestDTO) throws AccessDeniedException;
    String deletePage(long pageId) throws AccessDeniedException;
}
