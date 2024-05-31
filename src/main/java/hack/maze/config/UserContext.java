package hack.maze.config;

import hack.maze.entity.AppUser;

public class UserContext {

    private static final ThreadLocal<Long> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(Long userId) {
        currentUser.set(userId);
    }

    public static Long getCurrentUser() {
        return currentUser.get();
    }

    public static void clearContext() {
        currentUser.remove();
    }

}
