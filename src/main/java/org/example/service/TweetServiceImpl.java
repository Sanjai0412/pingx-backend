package org.example.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.inject.Inject;
import org.example.dto.TweetResponse;
import org.example.model.Tweet;
import org.example.repository.TweetRepository;

import java.util.ArrayList;
import java.util.List;

public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private static final int MAX_QUOTE_DEPTH = Integer.parseInt(Dotenv.load().get("MAX_QUOTE_DEPTH"));

    // HK2 reads this annotation and passes in the TweetRepositoryImpl obj
    @Inject
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    // To build tweet modal with its reposts with limit
    @Override
    public TweetResponse buildTweetResponse(Long tweetId, String currentUserId, int depth) {
        TweetResponse tweet = tweetRepository.getTweetById(tweetId, currentUserId);
        if (tweet == null) {
            return null;
        }

        if (depth > 0 && tweet.getQuoteTweetId() != null) {
            tweet.setQuotedTweet(
                    buildTweetResponse(
                            tweet.getQuoteTweetId(),
                            currentUserId,
                            depth - 1));
        }
        return tweet;
    }

    @Override
    public TweetResponse postNewTweet(Tweet tweet) {
        if (tweet.getContent() == null || tweet.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet content cannot be empty");
        }
        if (tweet.getContent().length() > 280) {
            throw new IllegalArgumentException("Tweet content exceeds 280 characters");
        }
        if (tweet.getUserId() == null || tweet.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required to tweet");
        }

        TweetResponse created;

        if (tweet.getQuoteTweetId() != null) {
            created = tweetRepository.createQuoteTweet(tweet);
        } else {
            created = tweetRepository.createTweet(tweet);
        }
        return buildTweetResponse(
                created.getId(),
                tweet.getUserId(),
                MAX_QUOTE_DEPTH);
    }

    @Override
    public List<TweetResponse> getAllTweets(String userId) {
        List<TweetResponse> tweets = new ArrayList<>();
        List<Long> tweetIds = tweetRepository.getAllTweetIds();

        for (Long id : tweetIds) {
            tweets.add(
                    buildTweetResponse(id, userId, MAX_QUOTE_DEPTH));
        }
        return tweets;
    }

    @Override
    public TweetResponse getTweetById(Long tweetId, String userId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be empty");
        }
        return buildTweetResponse(tweetId, userId, MAX_QUOTE_DEPTH);
    }

    @Override
    public List<TweetResponse> getTweetsByUserId(String currentUserId, String targetUserId) {
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        List<Long> tweetIds = tweetRepository.getTweetIdsByUserId(targetUserId);
        List<TweetResponse> tweets = new ArrayList<>();
        for (Long id : tweetIds) {
            tweets.add(
                    buildTweetResponse(id, currentUserId, MAX_QUOTE_DEPTH));
        }
        return tweets;
    }

    @Override
    public TweetResponse getRootTweet(Long tweetId, String currentUserId) {
        Tweet tweet = tweetRepository.fetchTweetById(tweetId);

        if (tweet == null)
            return null;

        while (tweet.getQuoteTweetId() != null) {
            tweet = tweetRepository.fetchTweetById(tweet.getQuoteTweetId());
        }
        return tweetRepository.getTweetById(tweet.getId(), currentUserId);
    }
}
