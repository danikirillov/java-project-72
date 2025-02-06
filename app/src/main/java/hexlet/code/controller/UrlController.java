package hexlet.code.controller;

import hexlet.code.dao.UrlRepository;
import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

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

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Integer.class).get();
        var url = UrlRepository.find(id)
                               .orElseThrow(() -> new NotFoundResponse("Url entity with id = " + id + " not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }
}
