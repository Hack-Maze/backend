package hack.maze.utils;

import hack.maze.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.AccessDeniedException;
import java.util.Objects;
@Slf4j
public class GlobalMethods {

    private static void checkUserAuthority(AppUser appUser, Long authorId) throws AccessDeniedException {
        if (appUser.getRole() != Role.ADMIN && !Objects.equals(appUser.getId(), authorId)) {
            throw new AccessDeniedException("Access denied!");
        }
    }

    public static void checkUserAuthority(AppUser appUser, Maze targetMaze) throws AccessDeniedException {
        checkUserAuthority(appUser, targetMaze.getAuthor().getAppUser().getId());
    }

    public static void checkUserAuthority(AppUser appUser, Page tagetPage) throws AccessDeniedException {
        checkUserAuthority(appUser, tagetPage.getMaze().getAuthor().getAppUser().getId());
    }

    public static void checkUserAuthority(AppUser appUser, Question targetQuestion) throws AccessDeniedException {
        checkUserAuthority(appUser, targetQuestion.getPage().getMaze().getAuthor().getAppUser().getId());
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
