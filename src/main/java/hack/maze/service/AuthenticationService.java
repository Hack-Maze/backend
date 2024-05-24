package hack.maze.service;

import hack.maze.dto.AuthenticationRequestDTO;
import hack.maze.dto.AuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponseDTO login(AuthenticationRequestDTO request, HttpServletResponse response) throws Exception;
}