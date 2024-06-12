package hack.maze.dto;

import lombok.Builder;

@Builder
public record QuestionDTO(String type, String content, String answer, String hint, int points) {
}
