package hack.maze.service;

import hack.maze.dto.ProfileLeaderboardDTO;

import java.time.LocalDate;
import java.util.List;

public interface ProfileQuestionProgressService {
    List<ProfileLeaderboardDTO> getLeaderboard(LocalDate start, LocalDate end);
}
