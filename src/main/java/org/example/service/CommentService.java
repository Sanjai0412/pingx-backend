package org.example.service;

import java.util.List;

import org.example.dto.CommentResponse;
import org.example.model.Comment;

public interface CommentService {
    CommentResponse postComment(Comment comment);

    CommentResponse getCommentById(Long commentId, String currentUserId);

    List<CommentResponse> getCommentsByTweetId(Long tweetId, String currentUserId);
}
