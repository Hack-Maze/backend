package hack.maze.controller;

import hack.maze.service.ProfileQuestionProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leadership")
public class LeaderShipController {

    private final ProfileQuestionProgressService profileQuestionProgressService;

    @GetMapping
    public ResponseEntity<?> getLeaderboard(@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end) {
        try {
            return ResponseEntity.ok(profileQuestionProgressService.getLeaderboard(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
