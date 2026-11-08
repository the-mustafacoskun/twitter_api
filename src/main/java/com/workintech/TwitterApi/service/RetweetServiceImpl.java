package com.workintech.TwitterApi.service;

import com.workintech.TwitterApi.dto.RetweetCountResponse;
import com.workintech.TwitterApi.dto.RetweetResponse;
import com.workintech.TwitterApi.entity.ReactionType;
import com.workintech.TwitterApi.entity.Retweet;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.repository.RetweetRepository;
import com.workintech.TwitterApi.repository.TweetRepository;
import com.workintech.TwitterApi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class RetweetServiceImpl implements RetweetService {
    @Autowired
    private RetweetRepository retweetRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public Map<Long,Integer> getRetweetCount() {
        List<RetweetCountResponse> retweetCountResponseList = retweetRepository.getRetweetCount();
        Map<Long,Integer> retweetCountResponseMap = new HashMap<>();
        for(RetweetCountResponse item : retweetCountResponseList) {
            retweetCountResponseMap.put(item.getTweetId(),item.getCount());
        }
        return retweetCountResponseMap;
    }

    @Override
    public Optional<Retweet> findByUser_IdAndTweet_Id(Long userId, Long tweetId) {
        return retweetRepository.findByUser_IdAndTweet_Id(userId,tweetId);
    }

    @Override
    public Long deleteByTweet_Id(Long tweetId) {
        return retweetRepository.deleteByTweet_Id(tweetId);
    }

    @Override
    public List<RetweetResponse> findByUserId(Long userId) {
        List<Retweet> retweets = retweetRepository.findByUser_Id(userId);
        List<RetweetResponse> retweetResponseList = new ArrayList<>();
        for(Retweet item : retweets) {

            retweetResponseList.add(new RetweetResponse(item.getId(),item.getTweet().getId(),item.getUser().getUserName(),item.getCreatedAt()));
        }
        return retweetResponseList;
    }

    public RetweetResponse toggleRetweet(Long userId, Long tweetId) {
        Optional<Retweet> optionalRetweet = retweetRepository.findByUser_IdAndTweet_Id(userId,tweetId);
        if(optionalRetweet.isPresent()) {
            retweetRepository.deleteByUser_IdAndTweet_Id(userId,tweetId);
            Retweet retweet = optionalRetweet.get();
            return new RetweetResponse(retweet.getId(),retweet.getTweet().getId(),retweet.getUser().getUserName(),retweet.getCreatedAt());
        }else{
            Retweet retweet = new Retweet();

            retweet.setUser(userRepository.findById(userId).orElseThrow(()->new ApiException("KUllanıcı Bulunamadı", HttpStatus.NOT_FOUND)));
            retweet.setTweet(tweetRepository.findById(tweetId).orElseThrow(()->new ApiException("KUllanıcı Bulunamadı", HttpStatus.NOT_FOUND)));
            Retweet savedRetweet = retweetRepository.save(retweet);
            return new RetweetResponse(savedRetweet.getId(),savedRetweet.getTweet().getId(),savedRetweet.getUser().getUserName(),savedRetweet.getCreatedAt());
        }
    }
}
