package com.workintech.TwitterApi.controller;


import com.workintech.TwitterApi.dto.TweetRequest;
import com.workintech.TwitterApi.dto.TweetResponse;
import com.workintech.TwitterApi.service.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping
    public ResponseEntity<TweetResponse> create(@Valid @RequestBody TweetRequest request,
                                                Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tweetService.create(request.content(), authentication.getName()));
    }

    @GetMapping
    public List<TweetResponse> findAll() {
        return tweetService.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/findByUserId")
    public List<TweetResponse> findByUserId(@RequestParam Long userId) {
        return tweetService.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/findById")
    public TweetResponse findById(@RequestParam Long id) {
        return tweetService.findById(id);
    }

    @PutMapping("/{id}")
    public TweetResponse update(@PathVariable Long id,
                                @Valid @RequestBody TweetRequest request,
                                Authentication authentication) {
        return tweetService.update(id, request.content(), authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        tweetService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
