package hack.maze.service;


import hack.maze.dto.BadgeDTO;
import hack.maze.entity.Badge;

import java.io.IOException;
import java.util.List;

public interface BadgeService {
    String createBadge(BadgeDTO badgeDTO) throws IOException;
    Badge getSingleBadge(long badgeId);
    String updateBadge(long badgeId, BadgeDTO badgeDTO) throws IOException;
    String deleteBadge(long badgeId);
    List<Badge> getAllBadges();
}
