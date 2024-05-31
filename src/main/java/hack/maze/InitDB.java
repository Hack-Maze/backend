package hack.maze;

import hack.maze.entity.*;
import hack.maze.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final BadgeRepo badgeRepo;
    private final TagRepo tagRepo;
    private final ProfileMazeProgressRepo profileMazeProgressRepo;
    private final ProfilePageProgressRepo profilePageProgressRepo;
    private final ProfileQuestionProgressRepo profileQuestionProgressRepo;

    private void init() {
        // create badge
        Badge savedBadge = badgeRepo.save(Badge
                .builder()
                .title("badge1")
                .image("image")
                .build());

        // create tag
        Tag savedTag = tagRepo.save(Tag
                .builder()
                .title("tag1")
                .build());

        // create appUser
        AppUser savedUser = userRepo.save(AppUser
                .builder()
                .email("user1@user1.user1")
                .username("user1")
                .password(passwordEncoder.encode("password1"))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build());

        // create profile
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
                .badges(List.of(savedBadge))
                .build());

        // create maze
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
                .tags(List.of(savedTag))
                .build());

        // create page
        Page savedPage = pageRepo.save(Page
                .builder()
                .title("title")
                .content("content")
                .description("desc")
                .maze(savedMaze)
                .build());

        // create question
        Question savedQuestion = questionRepo.save(Question
                .builder()
                .type("type")
                .content("content")
                .answer("answer")
                .page(savedPage)
                .build());

        // create user progress
        profileMazeProgressRepo.save(ProfileMazeProgress
                .builder()
                .profile(savedProfile)
                .isCompleted(false)
                .maze(savedMaze)
                .build());
        ProfilePageProgress savedProfilePageProgress = profilePageProgressRepo.save(ProfilePageProgress
                .builder()
                .page(savedPage)
                .profile(savedProfile)
                .isCompleted(false)
                .build());
        profileQuestionProgressRepo.save(ProfileQuestionProgress
                .builder()
                .question(savedQuestion)
                .profilePageProgress(savedProfilePageProgress)
                .build());
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            if (mazeRepo.count() == 0) {
                init();
            }
            if (userRepo.count() == 0) {
                AppUser savedUser = userRepo.save(AppUser
                        .builder()
                        .email("admin@admin.admin")
                        .username("admin")
                        .password("$2a$12$ugoECT/fnHBIRgczCJbe2eOKRDgKFrjTOKuG3EViEX3dk8HGo1r9C")
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .build());
                profileRepo.save(Profile.builder().appUser(savedUser).build());
            }


        };
    }

}
