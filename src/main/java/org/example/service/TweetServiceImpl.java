package org.example.service;

import jakarta.inject.Inject;
import org.example.dto.TweetResponse;
import org.example.model.Tweet;
import org.example.repository.TweetRepository;

import java.util.List;

public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;

    // HK2 reads this annotation and passes in the TweetRepositoryImpl obj
    @Inject
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public TweetResponse postNewTweet(Tweet tweet, String username) {
        if (tweet.getContent() == null || tweet.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet content cannot be empty");
        }
        if (tweet.getContent().length() > 280) {
            throw new IllegalArgumentException("Tweet content exceeds 280 characters");
        }
        if (tweet.getUserId() == null || tweet.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required to tweet");
        }
        return tweetRepository.createTweet(tweet, username);
    }

    @Override
    public List<TweetResponse> getAllTweets(String userId) {
        return tweetRepository.getAllTweets(userId);
    }

    @Override
    public Tweet getTweetById(Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be empty");
        }
        return tweetRepository.getTweetById(tweetId);
    }

    @Override
    public List<TweetResponse> getTweetsByUserId(String currentUserId, String targetUserId) {
        if (currentUserId == null || currentUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }
        return tweetRepository.getTweetsByUserId(currentUserId, targetUserId);
    }
}
