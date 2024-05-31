package hack.maze.service;

import hack.maze.entity.AppUser;

public interface UserService {
    AppUser getSingleUser(String username);
    AppUser findByEmail(String email);
    void checkIfUserWithEmailExists(String username);
    void checkIfUserWithUsernameExists(String email);
}
