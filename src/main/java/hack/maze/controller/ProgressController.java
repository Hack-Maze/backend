package hack.maze.controller;

import hack.maze.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/enroll-user-to-maze/{mazeId}")
    public ResponseEntity<?> enrollUserToMaze(@RequestParam long mazeId) {
        try {
            return ResponseEntity.ok(progressService.enrollUserToMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/record-user-to-page/{pageId}")
    public ResponseEntity<?> recordUserProgressInPage(@RequestParam long pageId) {
        try {
            return ResponseEntity.ok(progressService.recordUserProgressInPage(pageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/solve-question/{pageId}/{solvedQuestionId}")
    public ResponseEntity<?> solveQuestion(@RequestParam long pageId, @RequestParam long solvedQuestionId) {
        try {
            return ResponseEntity.ok(progressService.solveQuestion(pageId, solvedQuestionId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
