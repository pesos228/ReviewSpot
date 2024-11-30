package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.ReactionInputDto;

public interface ReactionService {
    void save(ReactionInputDto reactionInputDto);
    void deleteById(int id);
    void deleteByClientTargetAndType(int clientId, int targetId, String targetType);
    boolean isLike(int clientId, int targetId, String targetType);
    boolean isDislike(int clientId, int targetId, String targetType);

}
