package hack.maze.service.impl;

import hack.maze.dto.TagDTO;
import hack.maze.entity.Tag;
import hack.maze.repository.TagRepo;
import hack.maze.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static hack.maze.utils.GlobalMethods.nullMsg;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;

    @Override
    public String createTag(TagDTO tagDTO) {
        validateTagInfo(tagDTO);
        Tag tag = new Tag();
        tag.setTitle(tagDTO.title());
        tagRepo.save(tag);
        return "new tag with title = [" + tag.getTitle() + "] created successfully";
    }

    private void validateTagInfo(TagDTO tagDTO) {
        Objects.requireNonNull(tagDTO.title(), nullMsg("title"));
    }

    @Override
    public Tag getSingleTag(long tagId) {
        return tagRepo.findById(tagId).orElseThrow(() -> new RuntimeException("Tag with id = [" + tagId + "] not exist"));
    }

    @Override
    @Transactional
    public String updateTag(long tagId, TagDTO tagDTO) {
        Tag targetTag = getSingleTag(tagId);
        if (tagDTO.title() != null) {
            targetTag.setTitle(tagDTO.title());
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
