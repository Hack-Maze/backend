package hack.maze.controller;

import hack.maze.dto.CreateMazeDTO;
import hack.maze.dto.UpdateMazeDTO;
import hack.maze.repository.MazeRepo;
import hack.maze.service.MazeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/maze")
@RequiredArgsConstructor
public class MazeController {

    private final MazeService mazeService;
    private final MazeRepo mazeRepo;

    @PostMapping
    public ResponseEntity<String> createMaze(@ModelAttribute CreateMazeDTO createMazeDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mazeService.createMaze(createMazeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllMazes() {
        try {
            return ResponseEntity.ok(mazeService.getAllMazes());
//            return ResponseEntity.ok(mazeRepo.findAll());
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
    public ResponseEntity<?> updateMaze(@PathVariable long mazeId, @RequestBody UpdateMazeDTO updateMazeDTO) {
        try {
            return ResponseEntity.ok(mazeService.updateMaze(mazeId, updateMazeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
