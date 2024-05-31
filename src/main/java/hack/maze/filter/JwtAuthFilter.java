package hack.maze.filter;

import hack.maze.entity.AppUser;
import hack.maze.service.UserService;
import hack.maze.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static hack.maze.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
//    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);
        final long userId;
        final String jwt;

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(TOKEN_PREFIX.length());
        userId = (long) (int) jwtUtils.extractClaims(jwt).get("userId");

        if (!isAuthenticationEndpoint(request) && userId != 0 && SecurityContextHolder.getContext().getAuthentication() == null) {
            getUserAndPerformAuthentication(request, jwt, userId);
        } else {
            logger.warn("No Token Provided");
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticationEndpoint(HttpServletRequest request) {
        return request.getServletPath().contains("auth");
    }

    private void getUserAndPerformAuthentication(HttpServletRequest request, String jwt, long userId) {
        AppUser appUser = userService.getSingleUser(userId);
        if (jwtUtils.isTokenValid(jwt, appUser)) {
            authenticateUser(request, appUser);
        }
    }

    private void authenticateUser(HttpServletRequest request, AppUser appUser) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(appUser, null, appUser.getAuthorities()
                );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
