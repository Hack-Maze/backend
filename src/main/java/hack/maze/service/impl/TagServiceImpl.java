package hack.maze.service.impl;

import hack.maze.entity.Tag;
import hack.maze.repository.TagRepo;
import hack.maze.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;

    @Override
    public String createTag(Tag tag) {
        validateTagInfo(tag);
        tagRepo.save(tag);
        return "new tag with title = [" + tag.getTitle() + "] created successfully";
    }

    private void validateTagInfo(Tag tag) {
        Objects.requireNonNull(tag.getTitle());
    }

    @Override
    public Tag getSingleTag(long tagId) {
        return tagRepo.findById(tagId).orElseThrow(() -> new RuntimeException("Tag with id = [" + tagId + "] not exist"));
    }

    @Override
    @Transactional
    public String updateTag(long tagId, Tag tag) {
        Tag targetTag = getSingleTag(tagId);
        if (tag.getTitle() != null) {
            targetTag.setTitle(tag.getTitle());
        }
        return "Tag with id = [" + tagId + "] updated successfully";
    }

    @Override
    public String deleteTag(long tagId) {
        Tag targetTag = getSingleTag(tagId);
        tagRepo.delete(targetTag);
        return "tag with name = [" + targetTag.getTitle() + "] deleted successfully";
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }
}
