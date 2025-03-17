package hexlet.code.dto;

import hexlet.code.model.UrlCheck;

import java.util.Optional;

public record UrlWithLastCheck(Integer id, String name, Optional<UrlCheck> lastCheck) {
}
