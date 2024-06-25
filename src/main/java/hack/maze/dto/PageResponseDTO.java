package hack.maze.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponseDTO(Long id, String title, String description, String content, List<QuestionResponseDTO> questions , boolean isCompleted) {
}
