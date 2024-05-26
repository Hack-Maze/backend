package hack.maze.service;

import hack.maze.entity.Page;

import java.util.List;

public interface PageService {
    String createPage(long mazeId, Page page);
    List<Page> getAllPages();
    Page getSinglePage(long pageId);
    String updatePage(long pageId, Page page);
    String deletePage(long pageId);
}
