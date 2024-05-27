package hack.maze.controller;

import hack.maze.dto.CreateProfileDTO;
import hack.maze.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/update")
    public ResponseEntity<?> updateProfileInfo(@ModelAttribute CreateProfileDTO createProfileDTO) {
        try {
            return ResponseEntity.ok(profileService.updateProfileDate(createProfileDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/id/{profileId}")
    public ResponseEntity<?> getSingleProfileById(@PathVariable long profileId) {
        try {
            return ResponseEntity.ok(profileService.getSingleProfile(profileId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getSingleProfileByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(profileService.getSingleProfile(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
