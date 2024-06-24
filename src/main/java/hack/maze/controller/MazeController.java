package hack.maze.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hack.maze.dto.UpdateMazeDTO;
import hack.maze.service.MazeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/maze")
@RequiredArgsConstructor
public class MazeController {

    private final MazeService mazeService;

    @PostMapping
    public ResponseEntity<?> createMaze(@ModelAttribute UpdateMazeDTO updateMazeDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mazeService.createMaze(updateMazeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMazes() {
        try {
            return ResponseEntity.ok(mazeService.getAllMazes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{mazeId}")
    public ResponseEntity<?> getSingleMaze(@PathVariable long mazeId) {
        try {
            return ResponseEntity.ok(mazeService.getSingleMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{mazeId}")
    public ResponseEntity<?> deleteMaze(@PathVariable long mazeId) {
        try {
            return ResponseEntity.ok(mazeService.deleteMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{mazeId}")
    public ResponseEntity<?> updateMaze(@PathVariable long mazeId, @ModelAttribute UpdateMazeDTO updateMazeDTO) {
        try {
            return ResponseEntity.ok(mazeService.updateMaze(mazeId, updateMazeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/is-current-user-enrolled/{mazeId}")
    public ResponseEntity<?> isCurrentUserEnrolledInMaze(@PathVariable long mazeId) {
        try {
            return ResponseEntity.ok(mazeService.isCurrentUserEnrolledInMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/is-current-user-solver/{mazeId}")
    public ResponseEntity<?> isCurrentUserSolverInMaze(@PathVariable long mazeId) {
        try {
            return ResponseEntity.ok(mazeService.isCurrentUserSolverInMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/build-run/{mazeId}")
    public ResponseEntity<?> runImageBuildWorkFlow(@PathVariable Long mazeId) {
        try {
            return ResponseEntity.ok(mazeService.buildImageFromMaze(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/run-container/{mazeId}")
    public ResponseEntity<?> runContainer(@PathVariable Long mazeId) {
        try {
            return ResponseEntity.ok(mazeService.runContainer(mazeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @GetMapping("/solved-mazes")
    public ResponseEntity<?> getSolvedMazesByProfileId() {
        try {
            return ResponseEntity.ok(mazeService.getSolvedMazesByProfileId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/solved-mazes/{profileId}")
    public ResponseEntity<?> getSolvedMazesByProfileId(@PathVariable long profileId) {
        try {
            return ResponseEntity.ok(mazeService.getSolvedMazesByProfileId(profileId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    

}
