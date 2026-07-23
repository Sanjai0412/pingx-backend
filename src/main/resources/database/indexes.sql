-- =============================================================================
-- PingX Database Indexes
-- Run this script once against the database to maximize query performance.
-- Indexes are created with IF NOT EXISTS so the script is idempotent.
-- =============================================================================

-- ---------------------------------------------------------------------------
-- users
-- ---------------------------------------------------------------------------

-- Profile lookup by username (getUserByUsername, searchUsers LOWER(username))
CREATE INDEX IF NOT EXISTS idx_users_username
    ON users (username);

-- Full-text search on username / display_name via LOWER() LIKE queries.
-- A functional index on the lower-cased columns avoids a seq-scan.
CREATE INDEX IF NOT EXISTS idx_users_username_lower
    ON users (LOWER(username));

CREATE INDEX IF NOT EXISTS idx_users_display_name_lower
    ON users (LOWER(display_name));

-- ---------------------------------------------------------------------------
-- tweets
-- ---------------------------------------------------------------------------

-- Filter tweets by author and sort by time (getTweetIdsByUserId, getUserFeedActivities)
CREATE INDEX IF NOT EXISTS idx_tweets_user_id_created_at
    ON tweets (user_id, created_at DESC);

-- Filter reply tweets by their parent (getReplyTweetIdsByTweetId)
CREATE INDEX IF NOT EXISTS idx_tweets_parent_tweet_id
    ON tweets (parent_tweet_id)
    WHERE parent_tweet_id IS NOT NULL;

-- Filter / count quote-tweets (quote_count subquery in getTweetById / getTweetsById)
CREATE INDEX IF NOT EXISTS idx_tweets_quote_tweet_id
    ON tweets (quote_tweet_id)
    WHERE quote_tweet_id IS NOT NULL;

-- Global timeline sort used by getAllTweetIds
CREATE INDEX IF NOT EXISTS idx_tweets_created_at_desc
    ON tweets (created_at DESC);

-- ---------------------------------------------------------------------------
-- followers
-- ---------------------------------------------------------------------------

-- Count / list followers of a user (followed_id queries, home-feed subquery)
-- The composite PK (follower_id, followed_id) already covers follower_id lookups;
-- we need a separate index on followed_id for the reverse direction.
CREATE INDEX IF NOT EXISTS idx_followers_followed_id
    ON followers (followed_id);

-- ---------------------------------------------------------------------------
-- likes
-- ---------------------------------------------------------------------------

-- Aggregate like counts per tweet (GROUP BY tweet_id in all enriched queries).
-- The PK (user_id, tweet_id) covers point lookups but not the tweet_id-only scan.
CREATE INDEX IF NOT EXISTS idx_likes_tweet_id
    ON likes (tweet_id);

-- ---------------------------------------------------------------------------
-- retweets
-- ---------------------------------------------------------------------------

-- Aggregate retweet counts per tweet (GROUP BY tweet_id)
CREATE INDEX IF NOT EXISTS idx_retweets_tweet_id
    ON retweets (tweet_id);

-- Feed query: retweets by a user ordered by time (getUserFeedActivities / getHomeFeedActivities)
CREATE INDEX IF NOT EXISTS idx_retweets_user_id_retweeted_at
    ON retweets (user_id, retweeted_at DESC);

-- ---------------------------------------------------------------------------
-- notifications
-- ---------------------------------------------------------------------------

-- Fetch all notifications for a recipient sorted by time (getNotificationsByRecipientId)
CREATE INDEX IF NOT EXISTS idx_notifications_recipient_id_created_at
    ON notifications (recipient_id, created_at DESC);

-- Count unread notifications (getUnreadNotificationCount)
CREATE INDEX IF NOT EXISTS idx_notifications_recipient_id_is_read
    ON notifications (recipient_id, is_read)
    WHERE is_read = false;

-- Delete / lookup notifications by composite key (deleteNotification)
CREATE INDEX IF NOT EXISTS idx_notifications_recipient_actor_type
    ON notifications (recipient_id, actor_id, type);
