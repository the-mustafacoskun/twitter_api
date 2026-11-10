package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.CommentCountResponse;
import com.workintech.TwitterApi.dto.CommentRequest;
import com.workintech.TwitterApi.dto.CommentResponse;
import com.workintech.TwitterApi.entity.Comment;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.CommentRepository;
import com.workintech.TwitterApi.repository.TweetRepository;
import com.workintech.TwitterApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    // ---------- Okuma ----------

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> findByTweetIdOrderByCreatedAtDesc(Long tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new ApiException("Tweet bulunamadı", HttpStatus.NOT_FOUND);
        }
        return commentRepository.findByTweet_IdOrderByCreatedAtDesc(tweetId).stream()
                .map(this::mapToCommentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> getCommentCountMap() {
        Map<Long, Integer> countMap = new HashMap<>();
        for (CommentCountResponse item : commentRepository.getCommentCounts()) {
            countMap.put(item.getTweetId(), item.getCount().intValue());
        }
        return countMap;
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse findById(Long id) {
        return mapToCommentResponse(getComment(id));
    }

    // ---------- Yazma ----------

    @Override
    public CommentResponse save(Long tweetId, CommentRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND));
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ApiException("Tweet bulunamadı", HttpStatus.NOT_FOUND));

        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setUser(user);
        comment.setTweet(tweet);
        return mapToCommentResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse update(Long id, CommentRequest request, String username) {
        Comment comment = getComment(id);
        if (!comment.getUser().getUsername().equals(username)) {
            throw new ApiException("Sadece kendi yorumunuzu düzenleyebilirsiniz", HttpStatus.FORBIDDEN);
        }
        comment.setContent(request.content());
        return mapToCommentResponse(commentRepository.save(comment));
    }

    @Override
    public Long deleteByTweet_Id(Long tweetId) {
        return commentRepository.deleteByTweet_Id(tweetId);
    }

    @Override
    public Long deleteById(Long id, String username) {
        Comment comment = getComment(id);
        boolean isCommentOwner = comment.getUser().getUsername().equals(username);
        boolean isTweetOwner = comment.getTweet().getUser().getUsername().equals(username);
        if (!isCommentOwner && !isTweetOwner) {
            throw new ApiException("Bu yorumu silme yetkiniz yok", HttpStatus.FORBIDDEN);
        }
        commentRepository.delete(comment);
        return id;
    }

    // ---------- Yardımcılar ----------

    private Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Yorum bulunamadı", HttpStatus.NOT_FOUND));
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTweet().getId(),
                comment.getUser().getUsername());
    }
}