package hack.maze.service.impl;

import hack.maze.entity.AppUser;
import hack.maze.exceptions.UserAlreadyFoundException;
import hack.maze.repository.UserRepo;
import hack.maze.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public AppUser getSingleUser(long id) {
        return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id = [" + id + "] not found"));
    }

    @Override
    public void checkIfUserWithEmailExists(String email) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new UserAlreadyFoundException("User with email = [" + email + "] already exist");
        }
    }

    @Override
    public void checkIfUserWithUsernameExists(String username) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new UserAlreadyFoundException("User with username = [" + username + "] already exist");
        }
    }
}
