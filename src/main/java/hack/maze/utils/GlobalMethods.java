package hack.maze.utils;

import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Question;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

public class GlobalMethods {

    private static void checkUserAuthority(Long userId, Long userThatWantToGainAccess) throws AccessDeniedException {
        if (!Objects.equals(userId, userThatWantToGainAccess)) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static void checkUserAuthority(Long id, Maze targetMaze) throws AccessDeniedException {
        checkUserAuthority(id, targetMaze.getAuthor().getAppUser().getId());
    }

    public static void checkUserAuthority(Long id, Page tagetPage) throws AccessDeniedException {
        checkUserAuthority(id, tagetPage.getMaze().getAuthor().getAppUser().getId());
    }

    public static void checkUserAuthority(Long id, Question targetQuestion) throws AccessDeniedException {
        checkUserAuthority(id, targetQuestion.getPage().getMaze().getAuthor().getAppUser().getId());
    }

    public static String nullMsg(String s) {
        return String.format("[%s] shouldn't be null", s);
    }

}
