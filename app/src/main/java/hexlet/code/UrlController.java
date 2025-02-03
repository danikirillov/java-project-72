package hexlet.code;

import hexlet.code.dao.UrlRepository;
import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import io.javalin.http.Context;

import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class UrlController {
    public static void create(Context ctx) {
        final var flash = "flash";
        var url = ctx.formParam("url");

        try {
            var parsedUrl = URI.create(url).toURL();
            var domain = parsedUrl.getHost() + ":" +parsedUrl.getPort();
            var user = new Url(domain);
            UrlRepository.save(user);
            ctx.sessionAttribute(flash, "Страница успешно добавлена");
        }
        catch (MalformedURLException e) {
            ctx.sessionAttribute(flash, "Некорректный URL");
        }
        catch (SQLException e) {
            ctx.sessionAttribute(flash, "Страница уже существует");
        }
        
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("/index.jte", model("page", page));
    }
}
