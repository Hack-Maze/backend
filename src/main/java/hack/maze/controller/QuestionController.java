package hack.maze.controller;

import hack.maze.entity.Question;
import hack.maze.service.QuestionService;
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
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getAllQuestions() {
        try {
            return ResponseEntity.ok(questionService.getAllQuestions());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getSingleQuestion(@PathVariable long questionId) {
        try {
            return ResponseEntity.ok(questionService.getSingleQuestion(questionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{pageId}")
    public ResponseEntity<?> createQuestion(@PathVariable long pageId, @RequestBody Question page) {
        try {
            return ResponseEntity.ok(questionService.createQuestion(pageId, page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable long questionId, @RequestBody Question page) {
        try {
            return ResponseEntity.ok(questionService.updateQuestion(questionId, page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable long questionId) {
        try {
            return ResponseEntity.ok(questionService.deleteQuestion(questionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
