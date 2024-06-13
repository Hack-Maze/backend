package hack.maze.entity;

import lombok.Getter;

@Getter
public enum Level {
    NOOB(1000),
    SCRIPT_KIDDIE(2000),
    WANNABE(3000),
    CRACKER(4000),
    HACKER(8000),
    PRO_HACKER(12000),
    ELITE_HACKER(15000),
    LEGENDARY(17000),
    SUPERIOR(20000);

    private int value;
    private Level(int value) {
        this.value = value;
    }
}
