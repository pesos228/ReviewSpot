package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.CommentInputDto;
import com.webServer.ReviewSpot.dto.CommentOutputDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    void save(CommentInputDto commentInputDto);
    List<CommentOutputDto> findByClientId(int id);
    List<CommentOutputDto> findByClientIdAfterDate(int id, LocalDateTime date);
    List<CommentOutputDto> findByMediaIdAfterDate(int id, LocalDateTime date);
    List<CommentOutputDto> findByMediaId(int id);
    List<CommentOutputDto> getLastCommentsByClientId(int id, int count);
    Page<CommentOutputDto> getLastCommentsByClientId(int id,  int page, int size);
    Page<CommentOutputDto> getLastCommentsByMediaId(int id, int page, int size);
    void deleteById(int id);
    Page<CommentOutputDto> findAll(int page, int size);
}
