package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.dto.ReactionInputDto;
import com.webServer.ReviewSpot.entity.Client;
import com.webServer.ReviewSpot.entity.Comment;
import com.webServer.ReviewSpot.entity.Reaction;
import com.webServer.ReviewSpot.entity.Review;
import com.webServer.ReviewSpot.exceptions.*;
import com.webServer.ReviewSpot.repository.ClientRepository;
import com.webServer.ReviewSpot.repository.CommentRepository;
import com.webServer.ReviewSpot.repository.ReactionRepository;
import com.webServer.ReviewSpot.repository.ReviewRepository;
import com.webServer.ReviewSpot.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final ClientRepository clientRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository, ClientRepository clientRepository, ReviewRepository reviewRepository, CommentRepository commentRepository) {
        this.reactionRepository = reactionRepository;
        this.clientRepository = clientRepository;
        this.reviewRepository = reviewRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void save(ReactionInputDto reactionInputDto) {

        Client client = clientRepository.findById(reactionInputDto.getClientId());
        if (client == null){
            throw new ClientNotFoundException("Client with id:" + reactionInputDto.getClientId() + " not found");
        }

        if (reactionInputDto.getTargetType().equalsIgnoreCase("REVIEW")){
            Review review = reviewRepository.findById(reactionInputDto.getTargetId());
            if (review == null){
                throw new ReviewNotFoundException("Review with id: " + reactionInputDto.getTargetId() + " not found");
            }

            List<Reaction> reactions = reactionRepository.findByReviewId(review.getId());
            for (Reaction reaction: reactions){
                if (reaction.getClient().getId() == client.getId()){
                    throw new ClientAlreadyHaveReactionException("Client with id: " + client.getId() + " already have reaction on review with id: " + review.getId());
                }
            }

            Reaction reaction = new Reaction(client, review, reactionInputDto.isLike());
            reactionRepository.save(reaction);

        } else if (reactionInputDto.getTargetType().equalsIgnoreCase("COMMENT")) {
            Comment comment = commentRepository.findById(reactionInputDto.getTargetId());
            if (comment == null){
                throw new CommentNotFoundException("Comment with id: " + reactionInputDto.getTargetId() + " not found");
            }

            List<Reaction> reactions = reactionRepository.findByCommentId(comment.getId());
            for (Reaction reaction: reactions){
                if (reaction.getClient().getId() == client.getId()){
                    throw new ClientAlreadyHaveReactionException("Client with id: " + client.getId() + " already have reaction on comment with id: "+ comment.getId());
                }
            }

            Reaction reaction = new Reaction(client, comment, reactionInputDto.isLike());
            reactionRepository.save(reaction);
        }
        else{
            throw new UnknownTargetTypeException("TargetType: " + reactionInputDto.getTargetType() + " not found");
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Reaction reaction = reactionRepository.findById(id);
        if (reaction == null){
            throw new ReviewNotFoundException("Review with id: " + id + " not found");
        }

        reactionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByClientTargetAndType(int clientId, int targetId, String targetType) {
        Client client = clientRepository.findById(clientId);

        if (client == null){
            throw new ClientNotFoundException("Client with id:" + clientId + " not found");
        }

        for (Reaction reaction: findReactionList(targetType, targetId)){
            if (reaction.getClient().getId() == client.getId()){
                reactionRepository.deleteById(reaction.getId());
                return;
            }
        }
        throw new ClientReactionNotFoundException("Reaction not found for client with ID: " + client.getId());
    }

    @Override
    public boolean isLike(int clientId, int targetId, String targetType) {
        Client client = clientRepository.findById(clientId);

        if (client == null){
            System.err.println("Client with id:" + clientId + " not found");
            return false;
        }

        for (Reaction reaction: findReactionList(targetType, targetId)){
            if (reaction.getClient().getId() == client.getId()){
                return reaction.getLike();
            }
        }
        return false;
    }

    @Override
    public boolean isDislike(int clientId, int targetId, String targetType) {
        Client client = clientRepository.findById(clientId);

        if (client == null){
            System.err.println("Client with id:" + clientId + " not found");
            return false;
        }

        for (Reaction reaction: findReactionList(targetType, targetId)){
            if (reaction.getClient().getId() == client.getId()) {
                return !reaction.getLike();
            }
        }
        return false;
    }

    private List<Reaction> findReactionList(String targetType, int targetId){
        List<Reaction> reactions;
        if (targetType.equalsIgnoreCase("COMMENT")){
            reactions = reactionRepository.findByCommentId(targetId);
            if (reactions.isEmpty()){
                var comment = commentRepository.findById(targetId);
                if (comment == null){
                    throw new CommentNotFoundException("Comment with id: " + targetId + " not found");
                }
            }
        } else if (targetType.equalsIgnoreCase("REVIEW")) {
            reactions = reactionRepository.findByReviewId(targetId);
            if (reactions.isEmpty()){
                var review = reviewRepository.findById(targetId);
                if (review == null){
                    throw new ReviewNotFoundException("Review with id: " + targetId + " not found");
                }
            }
        }
        else{
            throw new UnknownTargetTypeException("TargetType: " + targetType + " not found");
        }
        return reactions;
    }
}
