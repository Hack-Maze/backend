package hack.maze.controller;

import hack.maze.entity.Tag;
import hack.maze.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        try {
            return ResponseEntity.ok(tagService.getAllTags());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(tagService.createTag(tag));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable long tagId, @RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(tagService.updateTag(tagId, tag));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable long tagId) {
        try {
            return ResponseEntity.ok(tagService.deleteTag(tagId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
