package hexlet.code.dto;

import hexlet.code.model.UrlCheck;

public record UrlWithLastCheck(Integer id, String name, UrlCheck lastCheck) {
}
