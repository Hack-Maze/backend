package hack.maze.service.impl;

import hack.maze.dto.AuthenticationRequestDTO;
import hack.maze.dto.AuthenticationResponseDTO;
import hack.maze.entity.AppUser;
import hack.maze.repository.UserRepo;
import hack.maze.service.AuthenticationService;
import hack.maze.utils.JwtUtils;
import jakarta.el.PropertyNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request, HttpServletResponse response) {
        try {
            return processAuthenticationAndTokenGeneration(request, response);
        } catch (BadCredentialsException bc) {
            log.error("حطأ في رقم الهاتف أو كلمة المرور");
            throw new IllegalStateException("حطأ في رقم الهاتف أو كلمة المرور");
        }
    }

    private AuthenticationResponseDTO processAuthenticationAndTokenGeneration(AuthenticationRequestDTO request, HttpServletResponse response) {
        validateAndAuthenticate(request);
        return createTokenAndSetToHeader(request, response);
    }

    private void validateAndAuthenticate(AuthenticationRequestDTO request) {
        log.info("Validating user request...");
        validateAuthenticationRequest(request);
        log.info("Trying to authenticate the user with username: {}", request.username());
        tryingToAuthenticateUser(request);
        log.info("User with username: {} authenticated Successfully", request.username());
    }

    private void validateAuthenticationRequest(AuthenticationRequestDTO request) {
        if (request.username() == null || request.password() == null) {
            log.error("You should provide all the required information [phoneNumber, password]");
            throw new PropertyNotFoundException("You should provide all the required information [phoneNumber, password]");
        }
    }

    private void tryingToAuthenticateUser(AuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username().trim(), request.password().trim()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, Object> getClaimsFromUser(AppUser appUser) {
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("user_id", appUser.getId());
        userInfo.put("role", appUser.getRole());
        userInfo.put("username", appUser.getUsername());

        return userInfo;
    }

    private AuthenticationResponseDTO createTokenAndSetToHeader(AuthenticationRequestDTO request, HttpServletResponse response) {
        AppUser appUser = loadUserByUsernameIfExist(request.username().trim());
        String token = createTokenFromUser(appUser);
        setTokenToHeaderAfterAuthSuccess(response, token);
        return AuthenticationResponseDTO
                .builder()
                .token(token)
                .role(appUser.getRole())
                .username(appUser.getUsername())
                .build();
    }

    private AppUser loadUserByUsernameIfExist(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("حطأ في اسم المستخدم أو كلمة المرور"));
    }

    private String createTokenFromUser(AppUser appUser) {
        return jwtUtils.generateToken(getClaimsFromUser(appUser), appUser);
    }

    private void setTokenToHeaderAfterAuthSuccess(HttpServletResponse response, String token) {
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}

