package org.example.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.inject.Inject;
import org.example.dto.TweetResponse;
import org.example.model.Tweet;
import org.example.model.NotificationType;
import org.example.repository.TweetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final NotificationService notificationService;
    private static final int MAX_QUOTE_DEPTH = Integer.parseInt(Dotenv.load().get("MAX_QUOTE_DEPTH"));

    // HK2 reads this annotation and passes in the TweetRepositoryImpl obj
    @Inject
    public TweetServiceImpl(TweetRepository tweetRepository, NotificationService notificationService) {
        this.tweetRepository = tweetRepository;
        this.notificationService = notificationService;
    }

    // To build tweet modal with its reposts with limit
    @Override
    public TweetResponse buildTweetResponse(Long tweetId, String currentUserId, int depth) {
        TweetResponse tweet = tweetRepository.getTweetById(tweetId, currentUserId);
        if (tweet == null) {
            return null;
        }

        List<TweetResponse> tweets = buildTweetResponses(
                List.of(tweetId), currentUserId, depth);

        return tweets.isEmpty() ? null : tweets.get(0);
    }

    // To build tweets modal with its reposts with limit (no n + 1 query problem)
    @Override
    public List<TweetResponse> buildTweetResponses(List<Long> tweetIds, String currentUserId, int depth) {
        if (tweetIds == null || tweetIds.isEmpty()) {
            return new ArrayList<TweetResponse>();
        }

        List<TweetResponse> tweets = tweetRepository.getTweetsById(tweetIds, currentUserId);

        Map<Long, TweetResponse> tweetMap = new HashMap<>(tweets.size());
        for (TweetResponse tweet : tweets) {
            tweetMap.put(tweet.getId(), tweet);
        }

        if (depth > 0) {
            Set<Long> quoteIds = new HashSet<>();
            for (TweetResponse tweet : tweets) {
                if (tweet.getQuoteTweetId() != null) {
                    quoteIds.add(tweet.getQuoteTweetId());
                }
            }
            if (!quoteIds.isEmpty()) {
                List<TweetResponse> quoteTweets = buildTweetResponses(new ArrayList<>(quoteIds), currentUserId,
                        depth - 1);

                Map<Long, TweetResponse> quotedMap = new HashMap<>(quoteTweets.size());

                for (TweetResponse quoteTweet : quoteTweets) {
                    quotedMap.put(quoteTweet.getId(), quoteTweet);
                }

                for (TweetResponse tweet : tweets) {
                    if (tweet.getQuoteTweetId() != null) {
                        TweetResponse quote = quotedMap.get(tweet.getQuoteTweetId());
                        if (quote != null) {
                            tweet.setQuotedTweet(quote);
                        }
                    }
                }
            }
        }
        List<TweetResponse> orderedResult = new ArrayList<>();
        for (Long id : tweetIds) {
            TweetResponse tweet = tweetMap.get(id);
            if (tweet != null) {
                orderedResult.add(tweet);
            }
        }
        return orderedResult;
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
            // Notify the owner of the quoted tweet
            Tweet quotedTweet = tweetRepository.fetchTweetById(tweet.getQuoteTweetId());
            if (quotedTweet != null) {
                notificationService.notify(
                        quotedTweet.getUserId(),
                        tweet.getUserId(),
                        NotificationType.QUOTE,
                        created.getId(),
                        null);
            }
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
        List<Long> tweetIds = tweetRepository.getAllTweetIds();

        return buildTweetResponses(tweetIds, userId, MAX_QUOTE_DEPTH);
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

        return buildTweetResponses(tweetIds, currentUserId, MAX_QUOTE_DEPTH);
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

    @Override
    public TweetResponse replyTweet(Tweet tweet) {
        if (tweet.getContent() == null || tweet.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Reply content cannot be empty");
        }
        if (tweet.getContent().length() > 280) {
            throw new IllegalArgumentException("Reply content exceeds 280 characters");
        }
        if (tweet.getUserId() == null || tweet.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweet.getParentTweetId() == null) {
            throw new IllegalArgumentException("Parent tweet ID is required for a reply");
        }

        // Verify parent tweet exists
        Tweet parent = tweetRepository.fetchTweetById(tweet.getParentTweetId());
        if (parent == null) {
            throw new IllegalArgumentException("Parent tweet not found");
        }

        TweetResponse created = tweetRepository.replyTweet(tweet);

        // Notify the owner of the parent tweet
        notificationService.notify(
                parent.getUserId(),
                tweet.getUserId(),
                NotificationType.REPLY,
                tweet.getParentTweetId(),
                null);

        return buildTweetResponse(created.getId(), tweet.getUserId(), MAX_QUOTE_DEPTH);
    }

    @Override
    public List<TweetResponse> getReplyTweetsByTweetId(Long tweetId, String currentUserId) {
        List<Long> replyIds = tweetRepository.getReplyTweetIdsByTweetId(tweetId);
        return buildTweetResponses(replyIds, currentUserId, MAX_QUOTE_DEPTH);
    }
}
