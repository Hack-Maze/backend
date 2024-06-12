package hack.maze.dto;

import lombok.Builder;

@Builder
public record QuestionResponseDTO(Long id, String type, String content, int points) {
}
