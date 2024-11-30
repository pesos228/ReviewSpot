package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CommentRepository {
    void save(Comment comment);
    Comment findById(int id);
    List<Comment> findByClientId(int id);
    List<Comment> findByClientIdAfterDate(int id, LocalDateTime date);
    List<Comment> findByMediaId(int id);
    List<Comment> findByMediaIdAfterDate(int id, LocalDateTime date);
    List<Comment> getLastCommentsByClientId(int id, int count);
    Page<Comment> getLastCommentsByMediaId(int id, Pageable pageable);
    void deleteById(int id);
}
