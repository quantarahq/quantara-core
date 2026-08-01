package dev.quantara.core.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, message, Map.of());
    }

    public static ErrorResponse validation(int status, String error, Map<String, String> fieldErrors) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, "Validation failed", fieldErrors);
    }
}
