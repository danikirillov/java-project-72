package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UrlsPage extends BasePage {
    private List<UrlWithLastCheck> urls;
}
