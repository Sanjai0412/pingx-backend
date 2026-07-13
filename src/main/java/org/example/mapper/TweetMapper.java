package org.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

import org.example.dto.TweetResponse;
import org.example.dto.UserResponse;

public class TweetMapper {
    public TweetResponse map(ResultSet resultSet) throws SQLException {
        TweetResponse tweet = new TweetResponse();
        UserResponse author = new UserResponse();
        tweet.setId(resultSet.getLong("id"));
        author.setUserId(resultSet.getString("user_id"));
        author.setDisplayName(resultSet.getString("display_name"));
        author.setUsername(resultSet.getString("username"));
        author.setProfileImgUrl(resultSet.getString("profile_img_url"));
        tweet.setAuthor(author); // set author details in tweet response
        tweet.setQuoteTweetId(resultSet.getLong("quote_tweet_id"));
        tweet.setContent(resultSet.getString("content"));
        tweet.setLikeCount(resultSet.getInt("like_count"));
        tweet.setRetweetCount(resultSet.getInt("retweet_count"));
        tweet.setLikedByCurrentUser(resultSet.getBoolean("liked_by_current_user"));
        tweet.setRetweetedByCurrentUser(resultSet.getBoolean("retweeted_by_current_user"));
        tweet.setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")));

        return tweet;
    }
}
