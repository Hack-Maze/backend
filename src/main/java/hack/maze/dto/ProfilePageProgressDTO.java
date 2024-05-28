package hack.maze.dto;

import hack.maze.entity.ProfileQuestionProgress;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfilePageProgressDTO(long id, MazeProfileDTO profile, PageProgressDTO page,
                                     List<ProfileQuestionProgress> questions, long totalNumberOfQuestions,
                                     long numberOfSolvedQuestions, boolean isCompleted) {
}
