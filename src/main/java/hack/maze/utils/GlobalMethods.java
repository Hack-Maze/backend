package hack.maze.utils;

import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Question;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.AccessDeniedException;
import java.util.Objects;
@Slf4j
public class GlobalMethods {

    private static void checkUserAuthority(Long userThatWantToGainAccess, Long authorId) throws AccessDeniedException {
        if (!Objects.equals(userThatWantToGainAccess, authorId)) {
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

    public static boolean isAuthEndpoint(HttpServletRequest request) {
        return request.getServletPath().contains("auth");
    }

    public static boolean isSwaggerEndpoint(HttpServletRequest request) {
        return request.getServletPath().contains("swagger");
    }

}
