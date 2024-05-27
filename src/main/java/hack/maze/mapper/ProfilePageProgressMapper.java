package hack.maze.mapper;

import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.ProfilePageProgress;
import hack.maze.entity.ProfileQuestionProgress;
import hack.maze.entity.Question;

import java.util.List;
import java.util.stream.Collectors;

import static hack.maze.mapper.PageMapper.fromPageToPageProgressDTO;
import static hack.maze.mapper.ProfileMapper.fromProfileToMazeProfileDTO;

public class ProfilePageProgressMapper {

    public static List<ProfilePageProgressDTO> fromProfilePageProgressToProfilePageProgressDTO(List<ProfilePageProgress> profilePageProgresses) {
        return profilePageProgresses.stream().map(ProfilePageProgressMapper::fromProfilePageProgressToProfilePageProgressDTO).collect(Collectors.toList());
    }

    public static ProfilePageProgressDTO fromProfilePageProgressToProfilePageProgressDTO(ProfilePageProgress profilePageProgress) {
        List<ProfileQuestionProgress> profileQuestionProgresses = profilePageProgress.getProfileQuestionProgresses();
        List<Question> questions = profilePageProgress.getPage().getQuestions();
        return ProfilePageProgressDTO
                .builder()
                .id(profilePageProgress.getId())
                .profile(fromProfileToMazeProfileDTO(profilePageProgress.getProfile()))
                .page(fromPageToPageProgressDTO(profilePageProgress.getPage()))
                .questions(profileQuestionProgresses)
                .totalNumberOfQuestions(questions.size())
                .numberOfSolvedQuestions(profileQuestionProgresses.size())
                .isCompleted(profileQuestionProgresses.size() == questions.size())
                .build();
    }

}
