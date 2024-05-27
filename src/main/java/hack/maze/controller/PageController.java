package hack.maze.controller;

import hack.maze.dto.PageRequestDTO;
import hack.maze.entity.Page;
import hack.maze.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/maze/{mazeId}")
    public ResponseEntity<?> getAllPagesInSpecificMaze(@PathVariable long mazeId) {
        try {
            return ResponseEntity.ok(pageService.getAllPagesInSpecificMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{pageId}")
    public ResponseEntity<?> getSinglePage(@PathVariable long pageId) {
        try {
            return ResponseEntity.ok(pageService.getSinglePage(pageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{mazeId}")
    public ResponseEntity<?> createPage(@PathVariable long mazeId, @RequestBody PageRequestDTO page) {
        try {
            return ResponseEntity.ok(pageService.createPage(mazeId, page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{pageId}")
    public ResponseEntity<?> updatePage(@PathVariable long pageId, @RequestBody PageRequestDTO page) {
        try {
            return ResponseEntity.ok(pageService.updatePage(pageId, page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{pageId}")
    public ResponseEntity<?> deletePage(@PathVariable long pageId) {
        try {
            return ResponseEntity.ok(pageService.deletePage(pageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
