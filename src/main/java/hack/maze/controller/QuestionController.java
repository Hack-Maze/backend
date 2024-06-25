package hack.maze.controller;

import hack.maze.dto.QuestionDTO;
import hack.maze.entity.Question;
import hack.maze.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getSingleQuestion(@PathVariable long questionId) {
        try {
            return ResponseEntity.ok(questionService.getSingleQuestion(questionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{pageId}")
    public ResponseEntity<?> createQuestion(@PathVariable long pageId, @RequestBody QuestionDTO questionDTO) {
        try {
            return ResponseEntity.ok(questionService.createQuestion(pageId, questionDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable long questionId, @RequestBody QuestionDTO questionDTO) {
        try {
            return ResponseEntity.ok(questionService.updateQuestion(questionId, questionDTO));
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

    @GetMapping("answer/{questionId}")
    public ResponseEntity<?> getQuestionAnswer(@PathVariable long questionId) {
        try {
            return ResponseEntity.ok(questionService.getQuestionAnswer(questionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("hint/{questionId}")
    public ResponseEntity<?> getQuestionHint(@PathVariable long questionId) {
        try {
            return ResponseEntity.ok(questionService.getQuestionHint(questionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/assign-key-to-question/{questionId}")
    public ResponseEntity<?> getQuestionHint(@PathVariable long questionId, @RequestParam("key") String key) {
        try {
            questionService.assignEnvToQuestion(questionId, key);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
