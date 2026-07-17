package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.example.config.DatabaseConnection;
import org.example.dto.FeedType;
import org.example.model.FeedActivity;

public class FeedRepositoryImpl implements FeedRepository {

    @Override
    public List<FeedActivity> getHomeFeedActivities(String currentUserId, int limit, int offset) {

        List<FeedActivity> feedActivities = new ArrayList<>();
        String sql = """
                    SELECT
                        t.id AS tweet_id,
                        t.user_id,
                        'TWEET' as activity_type,
                        t.created_at AS activity_at
                    FROM tweets t
                    WHERE (
                        t.user_id = ?
                        OR t.user_id IN (
                            SELECT followed_id
                            FROM followers
                            WHERE follower_id = ?
                        )
                    )
                    AND t.parent_tweet_id IS NULL
                    UNION ALL

                    SELECT
                        r.tweet_id,
                        r.user_id,
                        'RETWEET' as activity_type,
                        r.retweeted_at AS activity_at
                    FROM retweets r
                    WHERE r.user_id = ?
                        OR r.user_id IN (
                            SELECT followed_id
                            FROM followers
                            WHERE follower_id = ?
                        )

                    ORDER BY activity_at DESC
                    LIMIT ? OFFSET ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, currentUserId);
            preparedStatement.setString(2, currentUserId);
            preparedStatement.setString(3, currentUserId);
            preparedStatement.setString(4, currentUserId);
            preparedStatement.setInt(5, limit);
            preparedStatement.setInt(6, offset);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeedActivity feedActivity = new FeedActivity();
                feedActivity.setTweetId(resultSet.getLong("tweet_id"));
                feedActivity.setType(FeedType.valueOf(resultSet.getString("activity_type")));
                feedActivity.setPerformedByUserId(resultSet.getString("user_id"));
                feedActivity.setActivityAt(resultSet.getTimestamp("activity_at").toInstant().atZone(ZoneId.of("UTC")));

                feedActivities.add(feedActivity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return feedActivities;
    }

    @Override
    public List<FeedActivity> getUserFeedActivities(String targetUserId, int limit, int offset) {
        List<FeedActivity> feedActivities = new ArrayList<>();
        String sql = """
                    SELECT
                        t.id AS tweet_id,
                        t.user_id,
                        'TWEET' as activity_type,
                        t.created_at AS activity_at
                    FROM tweets t
                    WHERE t.user_id = ?
                    AND t.parent_tweet_id IS NULL
                    UNION ALL

                    SELECT
                        r.tweet_id,
                        r.user_id,
                        'RETWEET' as activity_type,
                        r.retweeted_at AS activity_at
                    FROM retweets r
                    WHERE r.user_id = ?

                    ORDER BY activity_at DESC
                    LIMIT ? OFFSET ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, targetUserId);
            preparedStatement.setString(2, targetUserId);
            preparedStatement.setInt(3, limit);
            preparedStatement.setInt(4, offset);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeedActivity feedActivity = new FeedActivity();
                feedActivity.setTweetId(resultSet.getLong("tweet_id"));
                feedActivity.setType(FeedType.valueOf(resultSet.getString("activity_type")));
                feedActivity.setPerformedByUserId(resultSet.getString("user_id"));
                feedActivity.setActivityAt(
                        resultSet.getTimestamp("activity_at").toInstant().atZone(ZoneId.of("UTC")));
                feedActivities.add(feedActivity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return feedActivities;
    }

}
