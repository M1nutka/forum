package com.noname.forum.dto.post;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PostRequest(
    @NotEmpty (message = "title cannot be empty")
    @Size (min = 3, max = 100, message = "title must be between 3 and 50 characters")
    String title,

    @NotEmpty (message = "description cannot be empty")
    @Min (value = 5, message = "description must min 5 char")
    String description
) {
} 