package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UrlCheck {
    private Integer id;

    private Integer statusCode;
    private String title;
    private String h1;
    private String description;
    private Integer urlId;
    private LocalDateTime createdAt;

    public UrlCheck(Integer statusCode, String title, String h1, String description, Integer urlId) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.urlId = urlId;
    }
}
