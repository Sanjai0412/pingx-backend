package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.example.dto.CommentResponse;
import org.example.model.Comment;
import org.example.repository.CommentRepository;

import jakarta.inject.Inject;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Inject
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentResponse postComment(Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        if (comment.getContent().length() > 280) {
            throw new IllegalArgumentException("Comment content exceeds 280 characters");
        }
        if (comment.getUserId() == null || comment.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required to post comment");
        }

        CommentResponse created;

        created = commentRepository.postComment(comment);

        return created;
    }

    @Override
    public CommentResponse getCommentById(Long commentId, String currentUserId) {
        if (commentId == null) {
            throw new IllegalArgumentException("Comment ID cannot be empty");
        }
        return commentRepository.getCommentById(commentId, currentUserId);
    }

    @Override
    public List<CommentResponse> getCommentsByTweetId(Long tweetId, String currentUserId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be empty");
        }
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("Current user ID is required to fetch comments");
        }
        List<Long> commentIds = commentRepository.getCommentsByTweetId(tweetId, currentUserId);

        if (commentIds.isEmpty()) {
            throw new IllegalArgumentException("No comments found for the given tweet ID");
        }

        List<CommentResponse> comments = new ArrayList<>();
        for (Long commentId : commentIds) {
            comments.add(commentRepository.getCommentById(commentId, currentUserId));
        }

        return comments;
    }

}
