package com.workintech.TwitterApi.dto;

import jakarta.validation.constraints.NotNull;

public record RetweetRequest(@NotNull Long id) {
}
