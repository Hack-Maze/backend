package hack.maze.service.impl;

import hack.maze.dto.AuthenticationRequestDTO;
import hack.maze.dto.AuthenticationResponseDTO;
import hack.maze.dto.RegisterDTO;
import hack.maze.entity.AppUser;
import hack.maze.entity.Profile;
import hack.maze.entity.Role;
import hack.maze.repository.ProfileRepo;
import hack.maze.repository.UserRepo;
import hack.maze.service.AuthenticationService;
import hack.maze.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;
    private final ProfileRepo profileRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public AuthenticationResponseDTO register(RegisterDTO request, HttpServletResponse response) {
        userService.checkIfUserWithUsernameExists(request.username());
        userService.checkIfUserWithEmailExists(request.email());
        AppUser appUser = AppUser
                .builder()
                .role(Role.USER)
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .createdAt(LocalDateTime.now())
                .build();
        AppUser savedUser = userRepo.save(appUser);
        Profile profile = Profile
                .builder()
                .appUser(savedUser)
                .build();
        profileRepo.save(profile);

        return createTokenAndSetToHeader(request, response);
    }

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request, HttpServletResponse response) {
        try {
            return processAuthenticationAndTokenGeneration(request, response);
        } catch (BadCredentialsException bc) {
            log.error("Wrong email or password");
            throw new UsernameNotFoundException("Wrong email or password");
        }
    }

    private AuthenticationResponseDTO processAuthenticationAndTokenGeneration(AuthenticationRequestDTO request, HttpServletResponse response) {
        validateAndAuthenticate(request);
        return createTokenAndSetToHeader(request, response);
    }

    private void validateAndAuthenticate(AuthenticationRequestDTO request) {
        log.info("Validating user request...");
        validateAuthenticationRequest(request);
        log.info("Trying to authenticate the user with email: {}", request.email());
        tryingToAuthenticateUser(request);
        log.info("User with email: {} authenticated Successfully", request.email());
    }

    private void validateAuthenticationRequest(AuthenticationRequestDTO request) {
        if (request.email() == null || request.password() == null) {
            log.error("You should provide all the required information [phoneNumber, password]");
            throw new PropertyNotFoundException("You should provide all the required information [phoneNumber, password]");
        }
    }

    private void tryingToAuthenticateUser(AuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim(), request.password().trim()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, Object> getClaimsFromUser(AppUser appUser) {
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("userId", appUser.getId());
        userInfo.put("role", appUser.getRole());

        return userInfo;
    }

    private AuthenticationResponseDTO createTokenAndSetToHeader(AuthenticationRequestDTO request, HttpServletResponse response) {
        AppUser appUser = loadUserByUsernameIfExist(request.email().trim());
        String token = createTokenFromUser(appUser);
        setTokenToHeaderAfterAuthSuccess(response, token);
        return AuthenticationResponseDTO
                .builder()
                .token(token)
                .role(appUser.getRole())
                .username(appUser.getUsername())
                .build();
    }

    private AuthenticationResponseDTO createTokenAndSetToHeader(RegisterDTO request, HttpServletResponse response) {
        AppUser appUser = loadUserByUsernameIfExist(request.email().trim());
        String token = createTokenFromUser(appUser);
        setTokenToHeaderAfterAuthSuccess(response, token);
        return AuthenticationResponseDTO
                .builder()
                .token(token)
                .role(appUser.getRole())
                .username(appUser.getUsername())
                .build();
    }

    private AppUser loadUserByUsernameIfExist(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Wrong email or password"));
    }

    private String createTokenFromUser(AppUser appUser) {
        return jwtUtils.generateToken(getClaimsFromUser(appUser), appUser);
    }

    private void setTokenToHeaderAfterAuthSuccess(HttpServletResponse response, String token) {
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}

