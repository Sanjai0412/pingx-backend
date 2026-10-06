package org.example.repository;

public class RetweetRepositoryImpl implements RetweetRepository {
    @Override
    public boolean retweetTweet(String userId, Long tweetId) {
        String sql = "INSERT INTO retweets (user_id, tweet_id) VALUES (?, ?)";
       return JdbcHelper.update(sql, userId, tweetId) > 0;
    }

    @Override
    public boolean undoRetweet(String userId, Long tweetId) {
        String sql = "DELETE FROM retweets WHERE user_id = ? AND tweet_id = ?";
        return JdbcHelper.update(sql, userId, tweetId) > 0;
    }

    @Override
    public boolean hasUserRetweetedTweet(String userId, Long tweetId) {
        String sql = "SELECT 1 FROM retweets WHERE user_id = ? AND tweet_id = ?";
        return JdbcHelper.queryOne(sql, rs -> rs.next(), userId, tweetId) != null;
    }

}
