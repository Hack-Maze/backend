package hack.maze.config;

import hack.maze.entity.AppUser;

public class UserContext {

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(String username) {
        currentUser.set(username);
    }

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static void clearContext() {
        currentUser.remove();
    }

}
