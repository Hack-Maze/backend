package hack.maze.service;

import hack.maze.dto.AuthenticationRequestDTO;
import hack.maze.dto.AuthenticationResponseDTO;
import hack.maze.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponseDTO register(RegisterDTO request, HttpServletResponse response) throws Exception;
    AuthenticationResponseDTO login(AuthenticationRequestDTO request, HttpServletResponse response) throws Exception;
}