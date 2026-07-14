package org.example.repository;

import java.util.List;

import org.example.model.FeedActivity;

public interface FeedRepository {
    List<FeedActivity> getHomeFeedActivities(String currentUserId, int limit, int offset);
}
