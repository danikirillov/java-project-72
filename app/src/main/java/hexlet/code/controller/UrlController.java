package hexlet.code.controller;

import hexlet.code.dao.UrlCheckRepository;
import hexlet.code.dao.UrlRepository;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlWithLastCheck;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class UrlController {
    public static final String FLASH_TYPE = "flashType";
    public static final String FLASH = "flash";

    public static void create(Context ctx) {
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
                ctx.sessionAttribute(FLASH, "Страница успешно добавлена");
                ctx.sessionAttribute(FLASH_TYPE, "alert-success");
            } else {
                ctx.sessionAttribute(FLASH, "Страница уже существует");
                ctx.sessionAttribute(FLASH_TYPE, "alert-warning");
            }
            ctx.redirect(NamedRoutes.urlsPath(), HttpStatus.forStatus(302));
        } catch (Exception e) {
            ctx.sessionAttribute(FLASH, "Некорректный URL");
            ctx.sessionAttribute(FLASH_TYPE, "alert-danger");
            ctx.redirect(NamedRoutes.homePath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var urlsWithLastCheck = new ArrayList<UrlWithLastCheck>();
        for (var url : urls ) {
            var lastCheck = UrlCheckRepository.findLastCheck(url.getId());
            urlsWithLastCheck.add(new UrlWithLastCheck(url.getId(), url.getName(), lastCheck));
        }

        var page = new UrlsPage(urlsWithLastCheck);
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(FLASH_TYPE));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Integer.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Урл с айди " + id + " не найден."));
        var checks = UrlCheckRepository.findAllByUrlId(id);
        var page = new UrlPage(url, checks);
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(FLASH_TYPE));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Integer.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Урл с айди " + id + " не найден."));

        try {
            var response = Unirest.get(url.getName()).asString();
            var responseDocument = Jsoup.parse(response.getBody());
            var urlCheck = new UrlCheck(
                response.getStatus(),
                responseDocument.title(),
                extractFirstH1(responseDocument),
                extractDescription(responseDocument),
                id
            );
            UrlCheckRepository.save(urlCheck);
            ctx.sessionAttribute(FLASH, "Страница успешно проверена");
            ctx.sessionAttribute(FLASH_TYPE, "alert-success");
        } catch (Exception e) {
            ctx.sessionAttribute(FLASH, e.getMessage());
            ctx.sessionAttribute(FLASH_TYPE, "alert-danger");
        }

        ctx.redirect(NamedRoutes.urlsPath(url.getId()));
    }

    private static String extractDescription(Document responseDocument) {
        return Optional.ofNullable(responseDocument.selectFirst("meta[name=description]"))
            .map(description -> description.attr("content"))
            .orElse("");
    }

    private static String extractFirstH1(Document responseDocument) {
        return Optional.ofNullable(responseDocument.selectFirst("h1"))
            .map(Element::text)
            .orElse("");
    }
}
