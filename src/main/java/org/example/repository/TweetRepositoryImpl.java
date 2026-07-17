package org.example.repository;

import java.util.List;
import org.example.config.DatabaseConnection;
import org.example.dto.TweetResponse;
import org.example.mapper.TweetMapper;
import org.example.model.Tweet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;

public class TweetRepositoryImpl implements TweetRepository {
    private final TweetMapper tweetMapper = new TweetMapper(); // Tweet mapper

    @Override
    public TweetResponse createTweet(Tweet tweet) {
        String sql = "INSERT INTO tweets (user_id, content) VALUES (?, ?)";
        String[] columnsToReturn = { "id", "user_id" }; // to return these fields
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, columnsToReturn)) {

            preparedStatement.setString(1, tweet.getUserId());
            preparedStatement.setString(2, tweet.getContent());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long tweetId = generatedKeys.getLong(1);
                        String userId = generatedKeys.getString(2);
                        return getTweetById(tweetId, userId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Tweet fetchTweetById(Long tweetId) {
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
                    tweet.setQuoteTweetId(resultSet.getLong("quote_tweet_id"));
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
    public TweetResponse createQuoteTweet(Tweet tweet) {
        String sql = "INSERT INTO tweets (user_id, content, quote_tweet_id) VALUES (?, ?, ?)";
        String[] columnsToReturn = { "id", "user_id", "content", "created_at" }; // to return these fields
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql, columnsToReturn)) {

            preparedStatement.setString(1, tweet.getUserId());
            preparedStatement.setString(2, tweet.getContent());
            preparedStatement.setLong(3, tweet.getQuoteTweetId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long tweetId = generatedKeys.getLong(1);
                        String userId = generatedKeys.getString(2);
                        return getTweetById(tweetId, userId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Long> getAllTweetIds() {
        List<Long> tweetIds = new ArrayList<>();
        String sql = "SELECT " +
                "    t.id " +
                "FROM tweets t " +
                "ORDER BY t.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tweetIds.add(resultSet.getLong("id"));
                }
                return tweetIds;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public TweetResponse getTweetById(Long tweetId, String userId) {
        String sql = "SELECT " +
                "    t.id, " +
                "    u.username, " +
                "    u.display_name, " +
                "    t.quote_tweet_id, " +
                "    t.parent_tweet_id, " +
                "    t.user_id, " +
                "    t.content, " +
                "    u.profile_img_url, " +
                "    COALESCE(l.like_count, 0) AS like_count, " +
                "    (COALESCE(r.retweet_count, 0) + COALESCE(q.quote_count, 0)) AS retweet_count, " +
                "    COALESCE(rep.reply_count, 0) AS reply_count, " +
                "    EXISTS ( " +
                "        SELECT 1 " +
                "        FROM likes l2 " +
                "        WHERE l2.tweet_id = t.id " +
                "          AND l2.user_id = ? " +
                "    ) AS liked_by_current_user, " +
                "    EXISTS ( " +
                "        SELECT 1 " +
                "        FROM retweets r2 " +
                "        WHERE r2.tweet_id = t.id " +
                "          AND r2.user_id = ? " +
                "    ) " +
                "        OR EXISTS ( " +
                "            SELECT 1 " +
                "            FROM tweets qt " +
                "            WHERE qt.quote_tweet_id = t.id " +
                "              AND qt.user_id = ? " +
                "        ) " +
                "AS retweeted_by_current_user, " +
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
                "ON t.id = r.tweet_id " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        quote_tweet_id, " +
                "        COUNT(*) AS quote_count " +
                "    FROM tweets " +
                "    WHERE quote_tweet_id IS NOT NULL " +
                "    GROUP BY quote_tweet_id " +
                ") q " +
                "    ON t.id = q.quote_tweet_id " +
                "LEFT JOIN ( " +
                "    SELECT " +
                "        parent_tweet_id, " +
                "        COUNT(*) AS reply_count " +
                "    FROM tweets " +
                "    WHERE parent_tweet_id IS NOT NULL " +
                "    GROUP BY parent_tweet_id " +
                ") rep " +
                "    ON t.id = rep.parent_tweet_id " +
                "WHERE t.id = ? " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3, userId);
            preparedStatement.setLong(4, tweetId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return tweetMapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Long> getTweetIdsByUserId(String targetUserId) {
        List<Long> tweetIds = new ArrayList<>();
        String sql = "SELECT " +
                "    t.id " +
                "    FROM tweets t " +
                "WHERE t.user_id = ? " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, targetUserId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tweetIds.add(resultSet.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tweetIds;
    }

    @Override
    public List<TweetResponse> getTweetsById(List<Long> tweetIds, String currentUserId) {
        if (tweetIds == null || tweetIds.isEmpty()) {
            return new ArrayList<TweetResponse>();
        }

        StringBuilder placeHoldersBuilder = new StringBuilder();
        for (int i = 0; i < tweetIds.size(); i++) {
            placeHoldersBuilder.append("?");
            if (i < tweetIds.size() - 1) {
                placeHoldersBuilder.append(",");
            }
        }
        String placeHolders = placeHoldersBuilder.toString();

        String sql = """
                    SELECT
                        t.id, u.username,
                        u.display_name, t.quote_tweet_id, t.parent_tweet_id,
                        t.user_id, t.content, u.profile_img_url,
                        COALESCE(l.like_count, 0) AS like_count,
                        (COALESCE(r.retweet_count, 0) + COALESCE(q.quote_count, 0)) AS retweet_count,
                        COALESCE(rep.reply_count, 0) AS reply_count,
                        EXISTS (
                            SELECT 1 FROM likes l2
                            WHERE l2.tweet_id = t.id AND l2.user_id = ?
                        ) AS liked_by_current_user,

                        EXISTS (
                            SELECT 1 FROM retweets r2
                            WHERE r2.tweet_id = t.id AND
                            r2.user_id = ?
                        ) AS retweeted_by_current_user,
                        t.created_at
                    FROM tweets t
                    JOIN users u ON t.user_id = u.id
                    LEFT JOIN (
                        SELECT tweet_id, COUNT(*) AS like_count
                        FROM likes
                        GROUP BY tweet_id
                    ) l ON t.id = l.tweet_id
                    LEFT JOIN (
                        SELECT tweet_id, COUNT(*) AS retweet_count
                        FROM retweets
                        GROUP BY tweet_id
                    ) r ON t.id = r.tweet_id
                    LEFT JOIN (
                        SELECT quote_tweet_id, COUNT(*) AS quote_count
                        FROM tweets
                        WHERE quote_tweet_id IS NOT NULL
                        GROUP BY quote_tweet_id
                    ) q ON t.id = q.quote_tweet_id
                    LEFT JOIN (
                        SELECT parent_tweet_id, COUNT(*) AS reply_count
                        FROM tweets
                        WHERE parent_tweet_id IS NOT NULL
                        GROUP BY parent_tweet_id
                    ) rep ON t.id = rep.parent_tweet_id
                """ + " WHERE t.id IN (" + placeHolders + ")";

        List<TweetResponse> tweets = new ArrayList<TweetResponse>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, currentUserId);
            preparedStatement.setString(2, currentUserId);

            // bind the list of tweet ids
            for (int i = 0; i < tweetIds.size(); i++) {
                preparedStatement.setLong(3 + i, tweetIds.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tweets.add(tweetMapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tweets;
    }

    @Override
    public TweetResponse replyTweet(Tweet tweet) {
        String sql = "INSERT INTO tweets (user_id, content, parent_tweet_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, tweet.getUserId());
            preparedStatement.setString(2, tweet.getContent());
            preparedStatement.setLong(3, tweet.getParentTweetId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long newId = resultSet.getLong("id");
                    // Fetch with full enrichment (counts, author, etc.)
                    return getTweetById(newId, tweet.getUserId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Long> getReplyTweetIdsByTweetId(Long tweetId) {
        String sql = """
                    SELECT t.id as tweet_id
                    FROM tweets t
                    WHERE t.parent_tweet_id = ?
                    ORDER BY t.created_at DESC
                """;
        List<Long> tweetIds = new ArrayList<Long>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, tweetId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tweetIds.add(resultSet.getLong("tweet_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tweetIds;
    }
}
