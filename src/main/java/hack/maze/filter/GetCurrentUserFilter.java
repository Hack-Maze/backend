package hack.maze.filter;

import hack.maze.config.UserContext;
import hack.maze.entity.AppUser;
import hack.maze.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static hack.maze.config.UserContext.clearContext;
import static hack.maze.config.UserContext.setCurrentUsername;
import static hack.maze.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class GetCurrentUserFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            if (!isAuthEndpoint(request)) {
                String authHeader = request.getHeader(AUTHORIZATION);
                String jwt = authHeader.substring(TOKEN_PREFIX.length());
                String username = jwtUtils.extractClaims(jwt).getSubject();
                setCurrentUsername(username);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
            clearContext();
        }

    }

    private boolean isAuthEndpoint(HttpServletRequest request) {
        return request.getServletPath().contains("auth");
    }
}