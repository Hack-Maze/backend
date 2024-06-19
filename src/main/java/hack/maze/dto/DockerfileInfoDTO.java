package hack.maze.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record DockerfileInfoDTO(Map<String, String> env, List<Integer> ports) {
}
