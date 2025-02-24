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

public class AppTest {
    Javalin app;
    
    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertEquals(response.code(), 200);
            assertNotNull(response.body());
            assertNotNull(response.body().string());
            assertTrue(response.body().string().contains("/urls"));
            assertTrue(response.body().string().contains("Hallo mein Freund"));
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertEquals(response.code(), 200);
            assertTrue(response.body().string().contains("Урлов нет"));
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "name=http://localhost:8080/hello";
            var response = client.post("/urls", requestBody);
            assertEquals(response.code(), 200);
            assertTrue(response.body().string().contains("Страница успешно добавлена"));
            var urlInDb = UrlRepository.findByName("http://localhost:8080");
            assertFalse(urlInDb.isEmpty());
        });
    }
    
    @Test
    public void testCreateUrlUnsuccessful() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "name=lhost:8080/hello";
            var response = client.post("/urls", requestBody);
            assertEquals(response.code(), 200);
            assertTrue(response.body().string().contains("Некорректный URL"));
            var urlInDb = UrlRepository.findByName("lhost:8080");
            assertTrue(urlInDb.isEmpty());
        });
    }
    
    @Test
    public void testShowUrl() throws SQLException {
        var name ="test";
        var url = new Url(name);
        UrlRepository.save(url);
        
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertEquals(response.code(), 200);
            assertTrue(response.body().string().contains(name));
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/-1");
            assertEquals(response.code(), 404);
        });
    }
}