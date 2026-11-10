package com.workintech.TwitterApi.controller;

import com.workintech.TwitterApi.dto.CommentRequest;
import com.workintech.TwitterApi.dto.CommentResponse;
import com.workintech.TwitterApi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestParam Long tweetId,
                                                  @Valid @RequestBody CommentRequest request,
                                                  Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.save(tweetId, request, authentication.getName()));
    }

    @GetMapping("/findByTweetId")
    public List<CommentResponse> findByTweetId(@RequestParam Long tweetId) {
        return commentService.findByTweetIdOrderByCreatedAtDesc(tweetId);
    }

    @GetMapping("/findById")
    public CommentResponse findById(@RequestParam Long id) {
        return commentService.findById(id);
    }


    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable Long id,
                                  @Valid @RequestBody CommentRequest request,
                                  Authentication authentication) {
        return commentService.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        commentService.deleteById(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}