package hack.maze.service;


import hack.maze.dto.BadgeDTO;
import hack.maze.entity.Badge;

import java.util.List;

public interface BadgeService {
    String createBadge(BadgeDTO badgeDTO);
    Badge getSingleBadge(long badgeId);
    String updateBadge(long badgeId, BadgeDTO badgeDTO);
    String deleteBadge(long badgeId);
    List<Badge> getAllBadges();
}
