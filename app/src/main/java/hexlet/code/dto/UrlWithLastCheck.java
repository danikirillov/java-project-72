package hexlet.code.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record UrlWithLastCheck(Integer id, String name, Optional<LocalDateTime> lastCheck) {
}
