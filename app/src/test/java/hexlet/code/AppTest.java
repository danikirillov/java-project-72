package hexlet.code;

import hexlet.code.dao.UrlCheckRepository;
import hexlet.code.dao.UrlRepository;
import hexlet.code.model.Url;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    Javalin app;
    static MockWebServer mockServer;

    @BeforeEach
    final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        var heyCom = readFixture("heycom.html");
        mockServer = new MockWebServer();
        var mockedResponse = new MockResponse().setBody(heyCom);
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
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

    private static Path getFixturePath(String fileName) {
        return Paths
            .get("src", "test", "resources", "fixtures", fileName)
            .toAbsolutePath()
            .normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        var fixturePath = getFixturePath(fileName);
        return Files.readString(fixturePath).trim();
    }

    @Test
    void testCheck() {
        JavalinTest.test(app, (server, client) -> {
            var testUrl = new Url(mockServer.url("/test").toString());
            UrlRepository.save(testUrl);

            client.post(String.format("/urls/%d/checks", testUrl.getId()));
            var actualCheckUrl = UrlCheckRepository.findAllByUrlId(testUrl.getId()).getFirst();

            assertNotNull(actualCheckUrl);
            assertEquals(200, actualCheckUrl.getStatusCode());
            assertEquals("Test title", actualCheckUrl.getTitle());
            assertEquals("test h1", actualCheckUrl.getH1());
            assertEquals("test description", actualCheckUrl.getDescription());
        });
    }

}
