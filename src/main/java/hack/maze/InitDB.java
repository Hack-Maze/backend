package hack.maze;

import hack.maze.entity.*;
import hack.maze.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final UserRepo userRepo;
    private final ProfileRepo profileRepo;
    private final MazeRepo mazeRepo;
    private final PageRepo pageRepo;
    private final QuestionRepo questionRepo;

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            AppUser savedUser = userRepo.save(AppUser
                    .builder()
                    .email("user1@user1.user1")
                    .username("user1")
                    .password("password1")
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .build());
            Profile savedProfile = profileRepo.save(Profile
                    .builder()
                    .appUser(savedUser)
                    .bio("bio")
                    .country("Egypt")
                    .fullName("user1user1")
                    .githubLink("githubLink")
                    .personalWebsite("personalWebsite")
                    .job("job")
                    .linkedinLink("linkedinLink")
                    .image("image")
                    .rank(100.0)
                    .build());
            Maze savedMaze = mazeRepo.save(Maze
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .image("image")
                    .author(savedProfile)
                    .description("desc")
                    .difficulty(Difficulty.EASY)
                    .summary("summary")
                    .visibility(true)
                    .title("title")
                    .enrolledUsers(List.of(savedProfile))
                    .solvers(List.of(savedProfile))
                    .build());
            Page savedPage = pageRepo.save(Page
                    .builder()
                    .title("title")
                    .content("content")
                    .description("desc")
                    .maze(savedMaze)
                    .build());

            questionRepo.save(Question
                    .builder()
                    .type("type")
                    .content("content")
                    .answer("answer")
                    .page(savedPage)
                    .build());
        };
    }

}
