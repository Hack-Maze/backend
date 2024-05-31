package hack.maze.utils;

import hack.maze.entity.AppUser;
import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Question;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class GlobalMethods {

    public static void checkUserAuthority(String username, Maze targetMaze) throws AccessDeniedException {
        if (!Objects.equals(username, targetMaze.getAuthor().getAppUser().getUsername())) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static void checkUserAuthority(String username, Page tagetPage) throws AccessDeniedException {
        if (!Objects.equals(username, tagetPage.getMaze().getAuthor().getAppUser().getUsername())) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static void checkUserAuthority(String username, Question targetQuestion) throws AccessDeniedException {
        if (!Objects.equals(username, targetQuestion.getPage().getMaze().getAuthor().getAppUser().getUsername())) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static String nullMsg(String s) {
        return String.format("[%s] shouldn't be null", s);
    }

}
