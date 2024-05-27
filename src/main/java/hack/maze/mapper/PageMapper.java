package hack.maze.mapper;

import hack.maze.dto.MazePageDTO;
import hack.maze.entity.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageMapper {

    public static List<MazePageDTO> fromPageToMazePageDTO(List<Page> pages) {
        return pages.stream().map(PageMapper::fromPageToMazePageDTO).collect(Collectors.toList());
    }

    public static MazePageDTO fromPageToMazePageDTO(Page page) {
        return MazePageDTO
                .builder()
                .id(page.getId())
                .title(page.getTitle())
                .description(page.getDescription())
                .build();
    }

}
