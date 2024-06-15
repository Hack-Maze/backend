package hack.maze.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QuestionProgressDTO(Long id, LocalDateTime solvedAt) {
}
