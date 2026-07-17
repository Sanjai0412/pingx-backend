package org.example.dto;

import jakarta.json.bind.annotation.JsonbTransient;

import java.time.ZonedDateTime;
import java.util.List;

public class TweetResponse {
    private Long id;
    private UserResponse author; // author's data
    private String content;
    private int likeCount;
    private int retweetCount;
    private int replyCount;
    private boolean retweetedByCurrentUser;
    private boolean likedByCurrentUser;
    private ZonedDateTime createdAt;

    private TweetResponse quotedTweet; // quoted tweet (if this is a quote post)
    private TweetResponse parentTweet; // parent tweet (if this is a reply)
    private List<TweetResponse> replies; // replies to this tweet

    @JsonbTransient
    private Long quoteTweetId;
    @JsonbTransient
    private Long parentTweetId;

    public TweetResponse() {
    }

    public TweetResponse(Long id, UserResponse author, String content, int likeCount,
            int retweetCount, int replyCount, Long quotedTweetId, Long parentTweetId,
            boolean retweetedByCurrentUser,
            boolean likedByCurrentUser,
            ZonedDateTime createdAt,
            TweetResponse quotedTweet, TweetResponse parentTweet) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likeCount = likeCount;
        this.retweetCount = retweetCount;
        this.replyCount = replyCount;
        this.quoteTweetId = quotedTweetId;
        this.parentTweetId = parentTweetId;
        this.likedByCurrentUser = likedByCurrentUser;
        this.retweetedByCurrentUser = retweetedByCurrentUser;
        this.createdAt = createdAt;
        this.quotedTweet = quotedTweet;
        this.parentTweet = parentTweet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Long getQuoteTweetId() {
        return this.quoteTweetId;
    }

    public void setQuoteTweetId(Long quoteTweetId) {
        this.quoteTweetId = quoteTweetId;
    }

    public boolean isRetweetedByCurrentUser() {
        return retweetedByCurrentUser;
    }

    public void setRetweetedByCurrentUser(boolean retweetedByCurrentUser) {
        this.retweetedByCurrentUser = retweetedByCurrentUser;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setQuotedTweet(TweetResponse quotedTweet) {
        this.quotedTweet = quotedTweet;
    }

    public TweetResponse getQuotedTweet() {
        return this.quotedTweet;
    }

    public Long getParentTweetId() {
        return this.parentTweetId;
    }

    public void setParentTweetId(Long parentTweetId) {
        this.parentTweetId = parentTweetId;
    }

    public TweetResponse getParentTweet() {
        return this.parentTweet;
    }

    public void setParentTweet(TweetResponse parentTweet) {
        this.parentTweet = parentTweet;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public List<TweetResponse> getReplies() {
        return replies;
    }

    public void setReplies(List<TweetResponse> replies) {
        this.replies = replies;
    }
}
