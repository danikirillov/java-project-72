package hexlet.code;

import hexlet.code.dao.UrlRepository;
import hexlet.code.model.Url;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    Javalin app;

    @BeforeEach
    final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertEquals(200, response.code());
            var responseBody = response.body().string();
            assertTrue(responseBody.contains("/urls"));
            assertTrue(responseBody.contains("Hallo mein Freund"));
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertEquals(200, response.code());
            var responseBody = response.body().string();
            assertTrue(responseBody.contains("Урлов нет"));
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=http://localhost:8080/hello";
            var response = client.post("/urls", requestBody);
            assertEquals(200, response.code());
            var urlInDb = UrlRepository.findByName("http://localhost:8080");
            assertFalse(urlInDb.isEmpty());
        });
    }

    @Test
    void testCreateUrlUnsuccessful() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=lhost:8080/hello";
            var response = client.post("/urls", requestBody);
            assertEquals(200, response.code());
            var urlInDb = UrlRepository.findByName("lhost:8080");
            assertTrue(urlInDb.isEmpty());
        });
    }

    @Test
    void testShowUrl() throws SQLException {
        var name = "test";
        var url = new Url(name);
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertEquals(200, response.code());
            var responseBody = response.body().string();
            assertTrue(responseBody.contains(name));
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/-1");
            assertEquals(404, response.code());
        });
    }
}