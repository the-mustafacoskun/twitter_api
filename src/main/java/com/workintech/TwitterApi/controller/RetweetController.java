package com.workintech.TwitterApi.controller;


import com.workintech.TwitterApi.dto.RetweetRequest;
import com.workintech.TwitterApi.dto.RetweetResponse;
import com.workintech.TwitterApi.entity.User;
import com.workintech.TwitterApi.exception.ApiException;
import com.workintech.TwitterApi.service.RetweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/retweet")
public class RetweetController {
    private final RetweetService retweetService;

    @PostMapping
    public ResponseEntity<RetweetResponse> retweet(@Valid @RequestBody RetweetRequest retweetRequest,
                                                   Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ApiException("Kullanıcı kimliği doğrulanamadı veya oturum açılmamış", HttpStatus.UNAUTHORIZED);
        }
        Long userId = user.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(retweetService.toggleRetweet(userId,retweetRequest.id()));
    }
    @GetMapping("/mine")
    public List<RetweetResponse> mine(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ApiException("Kullanıcı kimliği doğrulanamadı veya oturum açılmamış", HttpStatus.UNAUTHORIZED);
        }
        return retweetService.findByUserId(user.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RetweetResponse> toggle(@PathVariable Long id, Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ApiException("Kullanıcı kimliği doğrulanamadı veya oturum açılmamış", HttpStatus.UNAUTHORIZED);
        }
        Long userId = user.getId();
        RetweetResponse toggledRetweet = retweetService.toggleRetweet(userId,id);
        return ResponseEntity.ok(toggledRetweet);
    }

}
