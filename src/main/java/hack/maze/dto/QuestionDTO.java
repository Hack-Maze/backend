package hack.maze.dto;

import lombok.Builder;

@Builder
public record QuestionDTO(Long id, String type, String content, String answer, String hint) {
}
