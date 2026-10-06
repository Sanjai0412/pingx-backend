package org.example.repository;

public class LikeRepositoryImpl implements LikeRepository {
    @Override
    public boolean likeTweet(String userId, Long tweetId) {
        String sql = "INSERT INTO likes (user_id, tweet_id) VALUES (?, ?)";
        return JdbcHelper.update(sql, userId, tweetId) > 0;
    }

    @Override
    public boolean unlikeTweet(String userId, Long tweetId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND tweet_id = ?";
        return JdbcHelper.update(sql, userId, tweetId) > 0;
    }

    @Override
    public boolean isLiked(String userId, Long tweetId) {
        String sql = "SELECT 1 FROM likes WHERE user_id = ? AND tweet_id = ?";
        return JdbcHelper.queryOne(sql, rs -> rs.next(), userId, tweetId) != null;
    }


}
