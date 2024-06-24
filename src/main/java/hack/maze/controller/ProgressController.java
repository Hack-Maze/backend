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
            return ResponseEntity.ok(progressService.enrollInMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/solve-question/{questionId}")
    public ResponseEntity<?> solveQuestion(@PathVariable long questionId, @RequestParam("answer") String answer) {
        try {
            return ResponseEntity.ok(progressService.solveQuestion(questionId, answer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/get-profile-page-progress/{pageId}")
    public ResponseEntity<?> getProfilePagesProgress(@PathVariable long pageId) {
        try {
            return ResponseEntity.ok(progressService.getAllSolvedQuestionsInPage(pageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/mark-page-as-complete/{pageId}")
    public ResponseEntity<?> markPageAsComplete(@PathVariable long pageId) {
        try {
            return ResponseEntity.ok(progressService.markPageAsCompleted(pageId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/not-completed-mazes")
    public ResponseEntity<?> markPageAsComplete() {
        try {
            return ResponseEntity.ok(progressService.notCompletedMazes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
        // add week-progress endpoint to get the user progress in the current week using ProgressService.getCurrentUserProgressThisWeek()
    @GetMapping("/week-progress")
    public ResponseEntity<?> getCurrentUserProgressThisWeek() {
        try {
            return ResponseEntity.ok(progressService.getCurrentUserProgressThisWeek());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("current-level-progress")
    public ResponseEntity<?> getCurrentLevelProgress() {
        try {
            return ResponseEntity.ok(progressService.getCurrentLevelProgress());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
