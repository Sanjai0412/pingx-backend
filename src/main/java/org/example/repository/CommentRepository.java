package org.example.repository;

import java.util.List;

import org.example.dto.CommentResponse;
import org.example.model.Comment;

public interface CommentRepository {
    CommentResponse postComment(Comment comment);

    CommentResponse getCommentById(Long commentId, String currentUserId);

    List<Long> getCommentsByTweetId(Long tweetId, String currentUserId);
}
