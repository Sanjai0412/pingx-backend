package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.config.DatabaseConnection;
import org.example.dto.CommentResponse;
import org.example.mapper.CommentMapper;
import org.example.model.Comment;

public class CommentRepositoryImpl implements CommentRepository {

    private final CommentMapper commentMapper = new CommentMapper();

    @Override
    public CommentResponse postComment(Comment comment) {
        String sql = "INSERT INTO comments (tweet_id, user_id, content) VALUES (?, ?, ?)";
        String[] columnsToReturn = { "id", "tweet_id", "user_id" };
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, columnsToReturn)) {

            preparedStatement.setLong(1, comment.getTweetId());
            preparedStatement.setString(2, comment.getUserId());
            preparedStatement.setString(3, comment.getContent());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long commentId = generatedKeys.getLong(1);
                        String userId = generatedKeys.getString(2);

                        return getCommentById(commentId, userId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Long> getCommentsByTweetId(Long tweetId, String currentUserId) {
        List<Long> commentsIds = new ArrayList<>();
        String sql = """
                    SELECT
                        c.id
                    FROM comments c
                    WHERE c.tweet_id = ?
                    ORDER BY c.created_at DESC
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, tweetId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    commentsIds.add(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return commentsIds;
    }

    @Override
    public CommentResponse getCommentById(Long commentId, String currentUserId) {
        String sql = """
                SELECT
                c.id,
                c.tweet_id,
                c.content,
                c.parent_comment_id,
                c.created_at,
                c.user_id,
                u.username,
                u.display_name,
                u.profile_img_url,
                COALESCE(cl.like_count, 0) AS like_count,
                EXISTS (
                    SELECT 1
                    FROM comment_likes cl2
                    WHERE cl2.comment_id = c.id
                        AND cl2.user_id = ?
                ) AS liked_by_current_user
                FROM comments c
                JOIN users u
                    ON c.user_id = u.id
                LEFT JOIN (
                    SELECT
                        comment_id,
                        COUNT(*) AS like_count
                    FROM comment_likes
                    GROUP BY comment_id
                ) cl
                    ON c.id = cl.comment_id

                WHERE c.id = ?
                ORDER BY c.created_at DESC
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, currentUserId);
            preparedStatement.setLong(2, commentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return commentMapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
