package hack.maze.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PageDTO(Long id, String title, String description, String content, List<QuestionDTO> questions) {
}
