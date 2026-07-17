package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.dto.FeedResponse;
import org.example.dto.FeedType;
import org.example.dto.TweetResponse;
import org.example.model.FeedActivity;
import org.example.repository.FeedRepository;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.inject.Inject;

public class FeedServiceImpl implements FeedService {
    private final TweetService tweetService;
    private final FeedRepository feedRepository;
    private final UserService userService;
    private static final int MAX_QUOTE_DEPTH = Integer.parseInt(Dotenv.load().get("MAX_QUOTE_DEPTH"));

    @Inject
    public FeedServiceImpl(TweetService tweetService, FeedRepository feedRepository, UserService userService) {
        this.tweetService = tweetService;
        this.feedRepository = feedRepository;
        this.userService = userService;
    }

    @Override
    public List<FeedResponse> getHomeFeed(String currentUserId, int limit, int offset) {

        List<FeedActivity> feedActivities = feedRepository.getHomeFeedActivities(currentUserId, limit, offset);
        List<FeedResponse> feedResponses = new ArrayList<>();

        List<Long> tweetIds = new ArrayList<>();
        for (FeedActivity activity : feedActivities) {
            Long tweetId = activity.getTweetId();
            if (!tweetIds.contains(tweetId)) {
                tweetIds.add(tweetId);
            }
        }
        List<TweetResponse> tweets = tweetService.buildTweetResponses(tweetIds, currentUserId, MAX_QUOTE_DEPTH);

        Map<Long, TweetResponse> tweetMap = new HashMap<>();

        for (TweetResponse tweet : tweets) {
            tweetMap.put(tweet.getId(), tweet);
        }

        for (FeedActivity activity : feedActivities) {

            TweetResponse tweet = tweetMap.get(activity.getTweetId());

            if (tweet == null) {
                continue;
            }

            FeedResponse feedResponse = new FeedResponse();
            feedResponse.setTweet(tweet);
            feedResponse.setType(activity.getType());
            feedResponse.setActivityAt(activity.getActivityAt());

            if (activity.getType() == FeedType.TWEET) {
                feedResponse.setPerformedBy(tweet.getAuthor());
            } else {
                feedResponse.setPerformedBy(userService.getUserById(activity.getPerformedByUserId()));
            }
            feedResponses.add(feedResponse);
        }
        return feedResponses;
    }

    @Override
    public List<FeedResponse> getUserFeed(String targetUserId, String currentUserId, int limit, int offset) {

        List<FeedActivity> feedActivities = feedRepository.getUserFeedActivities(targetUserId, limit, offset);
        List<FeedResponse> feedResponses = new ArrayList<>();

        List<Long> tweetIds = new ArrayList<>();
        for (FeedActivity activity : feedActivities) {
            Long tweetId = activity.getTweetId();
            if (!tweetIds.contains(tweetId)) {
                tweetIds.add(tweetId);
            }
        }
        List<TweetResponse> tweets = tweetService.buildTweetResponses(tweetIds, currentUserId, MAX_QUOTE_DEPTH);

        Map<Long, TweetResponse> tweetMap = new HashMap<>();
        for (TweetResponse tweet : tweets) {
            tweetMap.put(tweet.getId(), tweet);
        }

        for (FeedActivity activity : feedActivities) {
            TweetResponse tweet = tweetMap.get(activity.getTweetId());
            if (tweet == null) {
                continue;
            }

            FeedResponse feedResponse = new FeedResponse();
            feedResponse.setTweet(tweet);
            feedResponse.setType(activity.getType());
            feedResponse.setActivityAt(activity.getActivityAt());

            if (activity.getType() == FeedType.TWEET) {
                feedResponse.setPerformedBy(tweet.getAuthor());
            } else {
                feedResponse.setPerformedBy(userService.getUserById(activity.getPerformedByUserId()));
            }
            feedResponses.add(feedResponse);
        }
        return feedResponses;
    }

}
