package hack.maze.mapper;

import hack.maze.dto.ProfilePageProgressDTO;
import hack.maze.entity.ProfilePageProgress;

import java.util.List;
import java.util.stream.Collectors;

import static hack.maze.mapper.QuestionMapper.fromQuestionToQuestionProgressDTO;

public class ProgressMapper {

    public static List<ProfilePageProgressDTO> fromProfilePageProgressToPageProgressDTO(List<ProfilePageProgress> profilePageProgresses) {
        return profilePageProgresses.stream().map(ProgressMapper::fromProfilePageProgressToPageProgressDTO).collect(Collectors.toList());
    }

    public static ProfilePageProgressDTO fromProfilePageProgressToPageProgressDTO(ProfilePageProgress profilePageProgress) {
        return ProfilePageProgressDTO
                .builder()
                .id(profilePageProgress.getId())
                .page(PageMapper.fromPageToPageProgressDTO(profilePageProgress.getPage()))
                .profile(ProfileMapper.fromProfileToMazeProfileDTO(profilePageProgress.getProfile()))
                .solvedQuestions(fromQuestionToQuestionProgressDTO(profilePageProgress.getQuestionProgresses()))
                .isCompleted(profilePageProgress.isCompleted())
                .build();
    }

}
