package hack.maze.service;

import hack.maze.entity.AppUser;

public interface UserService {
    AppUser getSingleUser(long id);
    void checkIfUserWithEmailExists(String username);
    void checkIfUserWithUsernameExists(String email);
}
