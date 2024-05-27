package hack.maze.service;

public interface ProgressService {
    String enrollUserToMaze(long mazeId);
    String recordUserProgressInPage(long pageId);
    String solveQuestion(long pageId, long solvedQuestionId);
}
