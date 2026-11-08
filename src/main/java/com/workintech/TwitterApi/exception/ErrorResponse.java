package com.workintech.TwitterApi.exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse{
    int status;
    String message;
    LocalDateTime timestamp;
}
