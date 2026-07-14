package org.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

import org.example.dto.CommentResponse;
import org.example.dto.UserResponse;

public class CommentMapper {
    public CommentResponse map(ResultSet resultSet) throws SQLException {
        CommentResponse comment = new CommentResponse();
        UserResponse author = new UserResponse();
        comment.setId(resultSet.getLong("id"));
        comment.setTweetId(resultSet.getLong("tweet_id"));
        comment.setContent(resultSet.getString("content"));
        Long parentCommentId = (Long) resultSet.getObject("parent_comment_id");
        comment.setParentCommentId(parentCommentId);
        comment.setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")));
        author.setUserId(resultSet.getString("user_id"));
        author.setUsername(resultSet.getString("username"));
        author.setDisplayName(resultSet.getString("display_name"));
        author.setProfileImgUrl(resultSet.getString("profile_img_url"));
        comment.setAuthor(author);
        return comment;
    }
}
