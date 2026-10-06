package org.example.repository;

public class FollowRepositoryImpl implements FollowRepository {
    @Override
    public boolean followUser(String followerId, String followingId) {
        String sql = "INSERT INTO followers (follower_id, followed_id) VALUES (?, ?)";
        return JdbcHelper.update(sql, followerId, followingId) > 0;
    }

    @Override
    public boolean unfollowUser(String followerId, String followingId) {
        String sql = "DELETE FROM followers WHERE follower_id = ? AND followed_id = ?";
        return JdbcHelper.update(sql, followerId, followingId) > 0;
    }

    @Override
    public boolean isFollowing(String followerId, String followingId) {
        String sql = "SELECT 1 FROM followers WHERE follower_id = ? AND followed_id = ?";
        return JdbcHelper.queryOne(sql, (rs) -> rs.next(), followerId, followingId) != null;
    }
}
