package hack.maze.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfilePageProgressDTO(long id, MazeProfileDTO profile, PageProgressDTO page,
                                     List<QuestionProgressDTO> solvedQuestions, boolean isCompleted) {
}
