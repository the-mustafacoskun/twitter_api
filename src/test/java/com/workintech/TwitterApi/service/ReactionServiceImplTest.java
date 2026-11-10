package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.repository.ReactionRepository;
import com.workintech.TwitterApi.repository.TweetRepository;
import com.workintech.TwitterApi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReactionServiceImplTest {


    private ReactionService reactionService;
    private UserService userService;
    private TweetService tweetService;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TweetRepository tweetRepository;

    @BeforeEach
    void setUp() {
        reactionService = new ReactionServiceImpl(reactionRepository,tweetRepository,userRepository);
    }
    @Test
    void findByUsernameAndTweetId() {
        String username = "testuser";
        Long tweetId = 1L;
        reactionService.findByUsernameAndTweetId(username, tweetId);
        verify(reactionRepository).findByUser_UsernameAndTweet_Id(username, tweetId);
    }

    @Test
    void getTweetLikeCounts() {
    }

    @Test
    void getTweetDislikeCounts() {
    }

    @Test
    void toggleReaction() {
    }

    @Test
    void deleteByTweet_Id() {
    }
}