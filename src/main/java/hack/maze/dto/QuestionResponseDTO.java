package hack.maze.dto;

import hack.maze.entity.QuestionType;
import lombok.Builder;

@Builder
public record QuestionResponseDTO(Long id, QuestionType type, String content, int points) {
}
