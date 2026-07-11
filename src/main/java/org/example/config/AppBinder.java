package org.example.config;

import jakarta.inject.Singleton;
import org.example.repository.*;
import org.example.service.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AppBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(UserRepositoryImpl.class).to(UserRepository.class).in(Singleton.class);
        bind(UserServiceImpl.class).to(UserService.class).in(Singleton.class);

        bind(TweetRepositoryImpl.class).to(TweetRepository.class).in(Singleton.class);
        bind(TweetServiceImpl.class).to(TweetService.class).in(Singleton.class);

        bind(LikeRepositoryImpl.class).to(LikeRepository.class).in(Singleton.class);
        bind(LikeServiceImpl.class).to(LikeService.class).in(Singleton.class);

        bind(FollowRepositoryImpl.class).to(FollowRepository.class).in(Singleton.class);
        bind(FollowServiceImpl.class).to(FollowService.class).in(Singleton.class);

        bind(ImageUploadServiceImpl.class).to(ImageUploadService.class).in(Singleton.class);
    }
}
