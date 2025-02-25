package hexlet.code.controller;

import hexlet.code.dao.UrlRepository;
import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class UrlController {
    public static void create(Context ctx) throws SQLException {
        final var flash = "flash";
        try {
            var urlParam = ctx.formParam("url");
            var parsedUrl = URI.create(urlParam).toURL();

            var domain = 
                String.format("%s://%s", parsedUrl.getProtocol(), parsedUrl.getAuthority())
                      .toLowerCase();
            
            var urlFromDb = UrlRepository.findByName(domain);
            if (urlFromDb.isEmpty()) {
                var url = new Url(domain);
                UrlRepository.save(url);
                ctx.sessionAttribute(flash, "Страница успешно добавлена");
            } else {
                ctx.sessionAttribute(flash, "Страница уже существует");
            }
        }
        catch (Exception e) {
            ctx.sessionAttribute(flash, "Некорректный URL");
        }
        ctx.redirect(NamedRoutes.homePath(), HttpStatus.forStatus(302));
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Integer.class).get();
        var url = UrlRepository.find(id)
                               .orElseThrow(() -> new NotFoundResponse("Урл с айди " + id + " не найден."));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }
}
