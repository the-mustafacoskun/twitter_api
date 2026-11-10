package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.RetweetCountResponse;
import com.workintech.TwitterApi.entity.Retweet;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class RetweetRepositoryTest {

    @Autowired
    private RetweetRepository retweetRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;


    User savedUser;
    Tweet savedTweet;

    @BeforeEach
    void setUp() {
        String uniqueId = java.util.UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername("test_" + uniqueId);
        user.setPassword("test");
        user.setEmail("test_" + uniqueId + "@test.com");
        user.setFirstName("test");
        user.setLastName("test");
        savedUser = userRepository.save(user);

        Tweet tweet = new Tweet();
        tweet.setUser(savedUser);
        tweet.setContent("test");
        savedTweet = tweetRepository.save(tweet);
    }

    @Test
    void getRetweetCount() {
        Retweet retweet = new Retweet();
        retweet.setUser(savedUser);
        retweet.setTweet(savedTweet);
        retweetRepository.save(retweet);
        List<RetweetCountResponse> retweetCount =retweetRepository.getRetweetCount();

        assertTrue(retweetCount.size()>=1);
    }

    @Test
    void findByUser_IdAndTweet_Id() {
        Retweet retweet = new Retweet();
        retweet.setUser(savedUser);
        retweet.setTweet(savedTweet);
        retweetRepository.save(retweet);
        Optional<Retweet> optionalRetweet = retweetRepository.findByUser_IdAndTweet_Id(savedUser.getId(),savedTweet.getId());
        assertTrue(optionalRetweet.isPresent());
        assertEquals(optionalRetweet.get().getUser(),savedUser);
        assertEquals(optionalRetweet.get().getTweet(),savedTweet);
    }

    @Test
    void findByUser_Id() {
        Retweet retweet = new Retweet();
        retweet.setUser(savedUser);
        retweet.setTweet(savedTweet);
        retweetRepository.save(retweet);
        List<Retweet>  retweets = retweetRepository.findByUser_Id(savedUser.getId());
        assertTrue(retweets.size()>=1);
        assertEquals(retweets.get(0).getUser(),savedUser);
        assertEquals(retweets.get(0).getTweet(),savedTweet);
    }

    @Test
    void deleteByTweet_Id() {
        Retweet retweet = new Retweet();
        retweet.setUser(savedUser);
        retweet.setTweet(savedTweet);
        retweetRepository.save(retweet);
        Long id = retweetRepository.deleteByTweet_Id(savedTweet.getId());
        assertTrue(id==1L);

    }

    @Test
    void deleteByUser_IdAndTweet_Id() {
        Retweet retweet = new Retweet();
        retweet.setUser(savedUser);
        retweet.setTweet(savedTweet);
        retweetRepository.save(retweet);
        assertNotNull(savedTweet.getId());
        retweetRepository.deleteByUser_IdAndTweet_Id(savedUser.getId(),savedTweet.getId());
        var deletedRetweet = retweetRepository.findByUser_IdAndTweet_Id(savedUser.getId(), savedTweet.getId());
        assertTrue(deletedRetweet.isEmpty());
    }
}