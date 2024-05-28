package hack.maze.service;

import hack.maze.dto.TagDTO;
import hack.maze.entity.Tag;

import java.util.List;

public interface TagService {
    String createTag(TagDTO tagDTO);
    Tag getSingleTag(long tagId);
    String updateTag(long tagId, TagDTO tagDTO);
    String deleteTag(long tagId);
    List<Tag> getAllTags();
}
