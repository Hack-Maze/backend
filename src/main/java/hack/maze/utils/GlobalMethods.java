package hack.maze.utils;

import hack.maze.entity.AppUser;
import hack.maze.entity.Maze;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class GlobalMethods {

    public static void checkUserAuthority(AppUser appUser, Maze targetMaze) throws AccessDeniedException {
        if (!Objects.equals(appUser.getUsername(), targetMaze.getAuthor().getAppUser().getUsername())) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static String nullMsg(String s) {
        return String.format("[%s] shouldn't be null", s);
    }

}
