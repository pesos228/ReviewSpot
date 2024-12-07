package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.dto.CommentInputDto;
import com.webServer.ReviewSpot.dto.CommentOutputDto;
import com.webServer.ReviewSpot.entity.Client;
import com.webServer.ReviewSpot.entity.Comment;
import com.webServer.ReviewSpot.entity.Media;
import com.webServer.ReviewSpot.entity.Reaction;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.exceptions.CommentNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.repository.ClientRepository;
import com.webServer.ReviewSpot.repository.CommentRepository;
import com.webServer.ReviewSpot.repository.MediaRepository;
import com.webServer.ReviewSpot.repository.ReactionRepository;
import com.webServer.ReviewSpot.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final MediaRepository mediaRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ReactionRepository reactionRepository, MediaRepository mediaRepository, ClientRepository clientRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.reactionRepository = reactionRepository;
        this.mediaRepository = mediaRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void save(CommentInputDto commentInputDto) {
        Client client = clientRepository.findById(commentInputDto.getClientId());
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + commentInputDto.getClientId() + " not found");
        }
        Media media = mediaRepository.findById(commentInputDto.getMediaId());
        if (media == null){
            throw new MediaNotFoundException("Media with id: " + commentInputDto.getMediaId() + " not found");
        }
        Comment comment = new Comment(client,media, LocalDateTime.now(),commentInputDto.getText());
        commentRepository.save(comment);
    }

    @Override
    public List<CommentOutputDto> findByClientId(int id) {
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + id + " not found");
        }

        List<Comment> comments = commentRepository.findByClientId(id);

        return comments.stream().map(comment -> modelMapper.map(comment, CommentOutputDto.class)).toList();
    }

    @Override
    public List<CommentOutputDto> findByClientIdAfterDate(int id, LocalDateTime date) {
        var client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with ID: " + id + " not found");
        }
        var comments = commentRepository.findByClientIdAfterDate(id, date);

        return comments.stream().map(comment -> modelMapper.map(comment, CommentOutputDto.class)).toList();
    }

    @Override
    public List<CommentOutputDto> findByMediaIdAfterDate(int id, LocalDateTime date) {
        var media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with ID: " + id + " not found");
        }
        var comments = commentRepository.findByMediaIdAfterDate(id, date);

        return comments.stream().map(comment -> modelMapper.map(comment, CommentOutputDto.class)).toList();
    }

    @Override
    public List<CommentOutputDto> findByMediaId(int id) {
        Media media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with id: " + id + " not found");
        }

        List<Comment> comments = commentRepository.findByMediaId(id);

        return comments.stream().map(comment -> modelMapper.map(comment, CommentOutputDto.class)).toList();
    }

    @Override
    public List<CommentOutputDto> getLastCommentsByClientId(int id, int count) {
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + id + " not found");
        }

        var comments = commentRepository.getLastCommentsByClientId(id, count);
        return comments.stream().map(comment -> modelMapper.map(comment, CommentOutputDto.class)).toList();
    }

    @Override
    public Page<CommentOutputDto> getLastCommentsByClientId(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + id + " not found");
        }

        var comments = commentRepository.getLastCommentsByClientId(id, pageable);

        return comments.map(comment -> modelMapper.map(comment, CommentOutputDto.class));
    }

    @Override
    public Page<CommentOutputDto> getLastCommentsByMediaId(int id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Media media = mediaRepository.findById(id);
        if (media == null){
            throw new ClientNotFoundException("Client with id: " + id + " not found1");
        }

        var comments = commentRepository.getLastCommentsByMediaId(id, pageable);

        return comments.map(comment -> modelMapper.map(comment, CommentOutputDto.class));
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        var comment = commentRepository.findById(id);
        if (comment == null){
            throw new CommentNotFoundException("Comment with id: " + id + " not found");
        }

        commentRepository.deleteById(id);
    }
}
