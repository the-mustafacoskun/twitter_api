package com.workintech.TwitterApi.repository;

import com.workintech.TwitterApi.dto.CommentCountResponse;
import com.workintech.TwitterApi.entity.Comment;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.entity.User;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TweetRepository tweetRepository;

    private User savedUser;
    private Tweet savedTweet;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("test");
        user.setLastName("Kalici");
        user.setFirstName("Kalici");
        user.setPassword("kalici");
        user.setEmail("ali@mail.com");
        savedUser = userRepository.save(user);

        Tweet tweet = new Tweet();
        tweet.setContent("Hello World");
        tweet.setUser(user);
        savedTweet= tweetRepository.save(tweet);
    }


    @DisplayName("Can find Comment by Tweet Id and Order By Desc")
    @Test
    void findByTweet_IdOrderByCreatedAtDesc() {
        Comment comment = new Comment();
        comment.setContent("Hello Comment");
        comment.setTweet(savedTweet);
        comment.setUser(savedUser);
        commentRepository.save(comment);

        List<Comment> comments = commentRepository.findByTweet_IdOrderByCreatedAtDesc(savedTweet.getId());
        assertFalse(comments.isEmpty());
        assertEquals("Hello Comment", comments.get(0).getContent());
        assertEquals(savedTweet, comments.get(0).getTweet());
        assertEquals(savedUser, comments.get(0).getUser());

    }
    @DisplayName("Can delete tweet by Tweet Id")
    @Test
    void deleteByTweet_Id() {
        Comment comment = new Comment();
        comment.setContent("Hello Comment");
        comment.setTweet(savedTweet);
        comment.setUser(savedUser);
        commentRepository.save(comment);


        Long don= commentRepository.deleteByTweet_Id(savedTweet.getId());

        assertTrue(don==1L);
    }

    @Test
    void getCommentCounts() {
        Comment comment = new Comment();
        comment.setContent("Hello Comment");
        comment.setTweet(savedTweet);
        comment.setUser(savedUser);
        commentRepository.save(comment);

        List<CommentCountResponse> commentCount=commentRepository.getCommentCounts();

        assertFalse(commentCount.isEmpty());
        assertTrue(commentCount.size()>1);
    }
}