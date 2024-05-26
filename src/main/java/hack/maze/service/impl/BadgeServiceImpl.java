package hack.maze.service.impl;

import hack.maze.dto.BadgeDTO;
import hack.maze.entity.Badge;
import hack.maze.repository.BadgeRepo;
import hack.maze.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepo badgeRepo;

    @Override
    public String createBadge(BadgeDTO badgeDTO) {
        Badge badge = new Badge();
        validateBadgeDTOInfo(badgeDTO);
        badge.setTitle(badgeDTO.title());
        badge.setImage(handleSendingImageToAzure(badgeDTO.image()));
        badgeRepo.save(badge);
        return "new badge with title = [" + badge.getTitle() + "] created successfully";
    }

    private String handleSendingImageToAzure(MultipartFile image) {
        return "";
    }

    private void validateBadgeDTOInfo(BadgeDTO badgeDTO) {
        Objects.requireNonNull(badgeDTO.title());
        Objects.requireNonNull(badgeDTO.image());
    }

    @Override
    public Badge getSingleBadge(long badgeId) {
        return badgeRepo.findById(badgeId).orElseThrow(() -> new RuntimeException("badge with id = [" + badgeId + "] not exist"));
    }

    @Override
    @Transactional
    public String updateBadge(long badgeId, BadgeDTO badgeDTO) {
        Badge targetBadge = getSingleBadge(badgeId);
        if (badgeDTO.title() != null) {
            targetBadge.setTitle(badgeDTO.title());
        }
        if (badgeDTO.image() != null) {
            targetBadge.setImage(handleSendingImageToAzure(badgeDTO.image()));
        }
        return "Badge updated successfully";
    }

    @Override
    public String deleteBadge(long badgeId) {
        Badge targetBadge = getSingleBadge(badgeId);
        badgeRepo.delete(targetBadge);
        return "Badge with title = [" + targetBadge.getTitle() + "] deleted successfully";
    }

    @Override
    public List<Badge> getAllBadges() {
        return badgeRepo.findAll();
    }
}
