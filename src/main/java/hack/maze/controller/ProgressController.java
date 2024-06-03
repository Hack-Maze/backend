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
    public ResponseEntity<?> enrollUserToMaze(@PathVariable long mazeId) {
        try {
            return ResponseEntity.ok(progressService.enrollUserToMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/record-user-to-page/{pageId}")
    public ResponseEntity<?> recordUserProgressInPage(@PathVariable long pageId) {
        try {
            return ResponseEntity.ok(progressService.recordUserProgressInPage(pageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/solve-question/{pageId}/{solvedQuestionId}")
    public ResponseEntity<?> solveQuestion(@PathVariable long pageId, @PathVariable long solvedQuestionId, @RequestParam("answer") String answer) {
        try {
            return ResponseEntity.ok(progressService.solveQuestion(pageId, solvedQuestionId, answer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-profile-maze-progress")
    public ResponseEntity<?> getProfileMazesProgress() {
        try {
            return ResponseEntity.ok(progressService.getProfileMazesProgress());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-profile-page-progress")
    public ResponseEntity<?> getProfilePagesProgress() {
        try {
            return ResponseEntity.ok(progressService.getProfilePagesProgress());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
