CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    bio TEXT,
    profile_img_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE tweets(
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    content VARCHAR(280) NOT NULL,
    quote_tweet_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Relationship : link tweets to author
CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
CONSTRAINT fk_quote FOREIGN KEY (quote_tweet_id) REFERENCES tweets(id) ON DELETE SET NULL
)

CREATE TABLE followers(
    follower_id VARCHAR(255) NOT NULL,
    followed_id VARCHAR(255) NOT NULL,
    followed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- to prevent User A cannot follow User B more than once
    PRIMARY KEY(follower_id, followed_id),

    CONSTRAINT fk_follower FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_followed FOREIGN KEY (followed_id) REFERENCES users(id) ON DELETE CASCADE
)

CREATE TABLE likes(
    user_id VARCHAR(255) NOT NULL,
    tweet_id BIGINT NOT NULL,
    liked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- to prevent multiple like on same tweet from a single user
    PRIMARY KEY(user_id, tweet_id),

    -- Relationship: link like to tweet and user
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_tweet FOREIGN KEY (tweet_id) REFERENCES tweets(id) ON DELETE CASCADE
)

CREATE TABLE retweets(
    user_id VARCHAR(255) NOT NULL,
    tweet_id BIGINT NOT NULL,
    retweeted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- to prevent multiple repost on same tweet from a single user
    PRIMARY KEY(user_id, tweet_id),

    -- Relationship: link retweet to the tweet and user
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_tweet FOREIGN KEY (tweet_id) REFERENCES tweets(id) ON DELETE CASCADE
)