package hack.maze.utils;

import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Question;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class GlobalMethods {

    private static void checkUserAuthority(String currentUsername, String userThatWantToGainAccess) throws AccessDeniedException {
        if (!Objects.equals(currentUsername, userThatWantToGainAccess)) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static void checkUserAuthority(String username, Maze targetMaze) throws AccessDeniedException {
        checkUserAuthority(username, targetMaze.getAuthor().getAppUser().getUsername());
    }

    public static void checkUserAuthority(String username, Page tagetPage) throws AccessDeniedException {
        checkUserAuthority(username, tagetPage.getMaze().getAuthor().getAppUser().getUsername());
    }

    public static void checkUserAuthority(String username, Question targetQuestion) throws AccessDeniedException {
        checkUserAuthority(username, targetQuestion.getPage().getMaze().getAuthor().getAppUser().getUsername());
    }

    public static String nullMsg(String s) {
        return String.format("[%s] shouldn't be null", s);
    }

}
