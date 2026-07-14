package org.example.service;

import java.util.List;

import org.example.dto.FeedResponse;

public interface FeedService {
    List<FeedResponse> getHomeFeed(String currentUserId, int limit, int offset);
}