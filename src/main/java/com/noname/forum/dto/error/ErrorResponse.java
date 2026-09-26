package com.noname.forum.dto.error;

import java.time.LocalDateTime;

public record ErrorResponse (
    String message,
    String detailsMessage,
    LocalDateTime errorTime
) {

}
