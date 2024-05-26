package hack.maze.service;

import hack.maze.entity.Tag;

import java.util.List;

public interface TagService {
    String createTag(Tag tag);
    Tag getSingleTag(long tagId);
    String updateTag(long tagId, Tag tag);
    String deleteTag(long tagId);
    List<Tag> getAllTags();
}
