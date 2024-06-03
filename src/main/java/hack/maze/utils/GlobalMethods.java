package hack.maze.utils;

import hack.maze.entity.Maze;
import hack.maze.entity.Page;
import hack.maze.entity.Question;
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
        log.info("[{}] -- [{}]", id, targetMaze.getAuthor().getAppUser().getId());
        checkUserAuthority(id, targetMaze.getAuthor().getAppUser().getId());
    }

    public static void checkUserAuthority(Long id, Page tagetPage) throws AccessDeniedException {
        log.info("[{}] -- [{}]",id, tagetPage.getMaze().getAuthor().getAppUser().getId() );
        checkUserAuthority(id, tagetPage.getMaze().getAuthor().getAppUser().getId());
    }

    public static void checkUserAuthority(Long id, Question targetQuestion) throws AccessDeniedException {
        log.info("[{}] -- [{}]", id, targetQuestion.getPage().getMaze().getAuthor().getAppUser().getId());
        checkUserAuthority(id, targetQuestion.getPage().getMaze().getAuthor().getAppUser().getId());
    }

    public static String nullMsg(String s) {
        return String.format("[%s] shouldn't be null", s);
    }

}
