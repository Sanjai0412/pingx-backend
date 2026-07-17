package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.example.dto.CommentResponse;
import org.example.model.Comment;
import org.example.model.NotificationType;
import org.example.repository.CommentRepository;
import org.example.repository.TweetRepository;

import jakarta.inject.Inject;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final NotificationService notificationService;

    @Inject
    public CommentServiceImpl(CommentRepository commentRepository, TweetRepository tweetRepository,
            NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.notificationService = notificationService;
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

        CommentResponse created = commentRepository.postComment(comment);
        if (created != null) {
            org.example.model.Tweet tweet = tweetRepository.fetchTweetById(comment.getTweetId());
            if (tweet != null) {
                notificationService.notify(
                        tweet.getUserId(),
                        comment.getUserId(),
                        NotificationType.COMMENT,
                        comment.getTweetId(),
                        created.getId());
            }
        }

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

        List<CommentResponse> comments = new ArrayList<>();
        if (commentIds.isEmpty()) {
            return comments;
        }

        for (Long commentId : commentIds) {
            comments.add(commentRepository.getCommentById(commentId, currentUserId));
        }

        return comments;
    }

}
