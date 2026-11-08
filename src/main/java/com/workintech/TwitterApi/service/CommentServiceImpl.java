package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.CommentCountResponse;
import com.workintech.TwitterApi.dto.CommentRequest;
import com.workintech.TwitterApi.dto.CommentResponse;
import com.workintech.TwitterApi.entity.Comment;
import com.workintech.TwitterApi.entity.Tweet;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.CommentRepository;
import com.workintech.TwitterApi.repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    private TweetRepository tweetRepository;

    private CommentResponse mapToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTweet().getId(),
                comment.getUser().getUserName()
               );
    }




    @Override
    public List<CommentResponse> findByTweetIdOrderByCreatedAtDesc(Long tweetId) {
        //TODO user kontrolü yapmalısın
        tweetRepository.findById(tweetId).orElseThrow(() ->new ApiException("Tweet bulunamadı", HttpStatus.NOT_FOUND));
        List<Comment> comments = commentRepository.findByTweetIdOrderByCreatedAtDesc(tweetId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponses.add(mapToCommentResponse(comment));
        }
        return commentResponses;
    }



    @Override
    public Map<Long,Integer> getCommentCountMap() {
        List<CommentCountResponse> commentCountResponseList = commentRepository.getCommentCounts();
        Map<Long,Integer> commentCountMap = new HashMap<>();
        for(CommentCountResponse item : commentCountResponseList) {
            commentCountMap.put(item.getTweetId(),item.getCount());
        }
        return commentCountMap;
    }

    @Override
    public CommentResponse findById(Long id) {
        return mapToCommentResponse(commentRepository.findById(id).orElseThrow(()->new ApiException("Yorum bulunamadı", HttpStatus.NOT_FOUND)));
    }

    @Override
    public CommentResponse save(Long tweetId, CommentRequest commentRequest) {
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(()->new ApiException("Tweet bulunamadı", HttpStatus.NOT_FOUND));
        Comment comment = new Comment();
        comment.setTweet(tweet);
        comment.setContent(commentRequest.content());
        //TODO user eklenecek
        commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }

    @Override
    public CommentResponse update(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->new ApiException("Yorum bulunamadı", HttpStatus.NOT_FOUND));
        comment.setContent(commentRequest.content());
        commentRepository.save(comment);
        return mapToCommentResponse(comment) ;
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
