package hack.maze.config;

import hack.maze.entity.AppUser;

public class UserContext {

    private static final ThreadLocal<AppUser> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(AppUser appUser) {
        currentUser.set(appUser);
    }

    public static AppUser getCurrentUser() {
        return currentUser.get();
    }

    public static void clearContext() {
        currentUser.remove();
    }

}
