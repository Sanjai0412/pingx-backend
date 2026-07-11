package org.example.repository;

import java.util.List;
import org.example.config.DatabaseConnection;
import org.example.dto.TweetResponse;
import org.example.model.Tweet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;

public class TweetRepositoryImpl implements TweetRepository {
    @Override
    public TweetResponse createTweet(Tweet tweet, String username) {
        String sql = "INSERT INTO tweets (user_id, content) VALUES (?, ?)";
        String[] columnsToReturn = { "id", "user_id", "content", "created_at" }; // to return these fields
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, columnsToReturn)) {

            preparedStatement.setString(1, tweet.getUserId());
            preparedStatement.setString(2, tweet.getContent());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {

                        TweetResponse tweetResponse = new TweetResponse();
                        tweetResponse.setId(generatedKeys.getLong(1));
                        tweetResponse.setUserId(generatedKeys.getString(2));
                        tweetResponse.setContent(generatedKeys.getString(3));
                        tweetResponse.setCreatedAt(generatedKeys.getTimestamp(4).toInstant().atZone(ZoneId.of("UTC")));
                        // set 0 as default
                        tweetResponse.setUsername(username); // from req body
                        tweetResponse.setLikeCount(0);
                        tweetResponse.setRetweetCount(0);
                        tweetResponse.setLikedByCurrentUser(false);

                        return tweetResponse;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<TweetResponse> getAllTweets(String userId) {
        List<TweetResponse> tweets = new ArrayList<>();
        String sql = "SELECT " +
                "    t.id, " +
                "    u.username, " +
                "    u.display_name, " +
                "    t.user_id, " +
                "    t.content, " +
                "    u.profile_img_url, " +
                "    COALESCE(l.like_count, 0) AS like_count, " +
                "    COALESCE(r.retweet_count, 0) AS retweet_count, " +
                "    EXISTS ( " +
                "        SELECT 1 " +
                "        FROM likes l2 " +
                "        WHERE l2.tweet_id = t.id " +
                "          AND l2.user_id = ? " +
                "    ) AS liked_by_current_user, " +
                "    t.created_at " +
                "FROM tweets t " +
                "JOIN users u " +
                "    ON t.user_id = u.id " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        tweet_id, " +
                "        COUNT(*) AS like_count " +
                "    FROM likes " +
                "    GROUP BY tweet_id " +
                ") l " +
                "    ON t.id = l.tweet_id " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        tweet_id, " +
                "        COUNT(*) AS retweet_count " +
                "    FROM retweets " +
                "    GROUP BY tweet_id " +
                ") r " +
                "    ON t.id = r.tweet_id " +
                "ORDER BY t.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TweetResponse tweet = new TweetResponse();
                    tweet.setId(resultSet.getLong("id"));
                    tweet.setUsername(resultSet.getString("username"));
                    tweet.setDisplayName(resultSet.getString("display_name"));
                    tweet.setUserId(resultSet.getString("user_id"));
                    tweet.setProfileImgUrl(resultSet.getString("profile_img_url"));
                    tweet.setContent(resultSet.getString("content"));
                    tweet.setLikeCount(resultSet.getInt("like_count"));
                    tweet.setRetweetCount(resultSet.getInt("retweet_count"));
                    tweet.setLikedByCurrentUser(resultSet.getBoolean("liked_by_current_user"));
                    tweet.setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")));

                    tweets.add(tweet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tweets;
    }

    @Override
    public Tweet getTweetById(Long tweetId) {
        String sql = "SELECT * FROM tweets WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, tweetId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Tweet tweet = new Tweet();
                    tweet.setId(resultSet.getLong("id"));
                    tweet.setUserId(resultSet.getString("user_id"));
                    tweet.setContent(resultSet.getString("content"));
                    tweet.setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")));

                    return tweet;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<TweetResponse> getTweetsByUserId(String currentUserId, String targetUserId) {
        List<TweetResponse> tweets = new ArrayList<>();
        String sql = "SELECT " +
                "    t.id, " +
                "    u.username, " +
                "    u.display_name, " +
                "    t.user_id, " +
                "    t.content, " +
                "    u.profile_img_url, " +
                "    COALESCE(l.like_count, 0) AS like_count, " +
                "    COALESCE(r.retweet_count, 0) AS retweet_count, " +
                "    EXISTS ( " +
                "        SELECT 1 " +
                "        FROM likes l2 " +
                "        WHERE l2.tweet_id = t.id " +
                "          AND l2.user_id = ? " +
                "    ) AS liked_by_current_user, " +
                "    t.created_at " +
                "FROM tweets t " +
                "JOIN users u " +
                "    ON t.user_id = u.id " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        tweet_id, " +
                "        COUNT(*) AS like_count " +
                "    FROM likes " +
                "    GROUP BY tweet_id " +
                ") l " +
                "    ON t.id = l.tweet_id " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        tweet_id, " +
                "        COUNT(*) AS retweet_count " +
                "    FROM retweets " +
                "    GROUP BY tweet_id " +
                ") r " +
                "    ON t.id = r.tweet_id " +
                "WHERE t.user_id = ? " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, currentUserId);
            preparedStatement.setString(2, targetUserId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TweetResponse tweet = new TweetResponse();
                    tweet.setId(resultSet.getLong("id"));
                    tweet.setUsername(resultSet.getString("username"));
                    tweet.setDisplayName(resultSet.getString("display_name"));
                    tweet.setUserId(resultSet.getString("user_id"));
                    tweet.setProfileImgUrl(resultSet.getString("profile_img_url"));
                    tweet.setContent(resultSet.getString("content"));
                    tweet.setLikeCount(resultSet.getInt("like_count"));
                    tweet.setRetweetCount(resultSet.getInt("retweet_count"));
                    tweet.setLikedByCurrentUser(resultSet.getBoolean("liked_by_current_user"));
                    tweet.setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")));

                    tweets.add(tweet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tweets;
    }
}
