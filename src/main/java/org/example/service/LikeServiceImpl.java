package org.example.service;

import jakarta.inject.Inject;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.repository.LikeRepository;
import org.example.repository.TweetRepository;

public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    @Inject
    public LikeServiceImpl(LikeRepository likeRepository, TweetRepository tweetRepository){
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public boolean likeTweet(String userId, Long tweetId) {
        if(tweetId == null){
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if(userId.trim().isEmpty()){
            throw new IllegalArgumentException("User ID is required");
        }
        if(tweetRepository.getTweetById(tweetId) == null){
            throw new NotFoundException("Tweet not found");
        }
        if(likeRepository.isLiked(userId, tweetId)){
            throw new ConflictException("Tweet already liked");
        }
        return likeRepository.likeTweet(userId, tweetId);
    }

    @Override
    public boolean unlikeTweet(String userId, Long tweetId) {
        if(tweetId == null){
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if(userId.trim().isEmpty()){
            throw new IllegalArgumentException("User ID is required");
        }
        if(tweetRepository.getTweetById(tweetId) == null){
            throw new NotFoundException("Tweet not found");
        }
        if(!likeRepository.isLiked(userId, tweetId)){
            throw new ConflictException("Tweet never liked");
        }
        return likeRepository.unlikeTweet(userId, tweetId);
    }

    @Override
    public boolean hasUserLikedTweet(String userId, Long tweetId) {
        if(tweetId == null){
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if(userId.trim().isEmpty()){
            throw new IllegalArgumentException("User ID is required");
        }
        return likeRepository.isLiked(userId, tweetId);
    }

    @Override
    public long getLikeCount(Long tweetId) {
        if(tweetId == null){
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        return likeRepository.getLikesCount(tweetId);
    }
}
