package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.TweetResponse;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.TweetRepository;
import com.workintech.TwitterApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final ReactionService reactionService;
    private final RetweetService retweetService;
    private final CommentService commentService;

    //  Okuma

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponse> findByUsername(String username) {
        return toResponseList(tweetRepository.findByUser_UsernameOrderByCreatedAtDesc(username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponse> findAllByOrderByCreatedAtDesc() {
        return toResponseList(tweetRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponse> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return toResponseList(tweetRepository.findByUser_IdOrderByCreatedAtDesc(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public TweetResponse findById(Long id) {
        return toResponse(getTweet(id));
    }

    //  Yazma

    @Override
    public TweetResponse create(String content, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));

        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setUser(user);
        return toResponse(tweetRepository.save(tweet));
    }

    @Override
    public TweetResponse update(Long id, String content, String username) {
        Tweet tweet = getTweet(id);
        checkOwner(tweet, username);
        tweet.setContent(content);
        return toResponse(tweetRepository.save(tweet));
    }

    @Override
    public void delete(Long id, String username) {
        Tweet tweet = getTweet(id);
        checkOwner(tweet, username);

        // Foreign key'ler yüzünden önce bağlı kayıtlar silinir
        retweetService.deleteByTweet_Id(id);
        reactionService.deleteByTweet_Id(id);
        commentService.deleteByTweet_Id(id);
        tweetRepository.delete(tweet);
    }

    // ---------- Yardımcılar ----------

    private Tweet getTweet(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Tweet Bulunamadı", HttpStatus.NOT_FOUND));
    }

    private void checkOwner(Tweet tweet, String username) {
        if (!tweet.getUser().getUsername().equals(username)) {
            throw new ApiException("Bu tweet size ait değil", HttpStatus.FORBIDDEN);
        }
    }

    private TweetResponse toResponse(Tweet tweet) {
        return toResponseList(List.of(tweet)).get(0);
    }

    // Sayaç haritaları liste başına BİR kez üretilir, tweet başına değil
    private List<TweetResponse> toResponseList(List<Tweet> tweets) {
        Map<Long, Integer> likes = reactionService.getTweetLikeCounts();
        Map<Long, Integer> dislikes = reactionService.getTweetDislikeCounts();
        Map<Long, Integer> retweets = retweetService.getRetweetCount();
        Map<Long, Integer> comments = commentService.getCommentCountMap();

        return tweets.stream()
                .map(t -> new TweetResponse(
                        t.getId(),
                        t.getContent(),
                        t.getCreatedAt(),
                        t.getUser().getId(),
                        t.getUser().getUsername(),
                        likes.getOrDefault(t.getId(), 0),
                        dislikes.getOrDefault(t.getId(), 0),
                        retweets.getOrDefault(t.getId(), 0),
                        comments.getOrDefault(t.getId(), 0)))
                .toList();
    }
}