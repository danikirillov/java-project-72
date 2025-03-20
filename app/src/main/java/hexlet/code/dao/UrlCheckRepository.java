package hexlet.code.dao;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql =
            "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var createdAt = LocalDateTime.now();
            statement.setInt(1, urlCheck.getStatusCode());
            statement.setString(2, urlCheck.getTitle());
            statement.setString(3, urlCheck.getH1());
            statement.setString(4, urlCheck.getDescription());
            statement.setInt(5, urlCheck.getUrlId());
            statement.setTimestamp(6, Timestamp.valueOf(createdAt));
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getInt(1));
                urlCheck.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id after saving the entity");
            }
        }
    }

    public static List<UrlCheck> findAllByUrlId(Integer urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, urlId);
            var resultSet = statement.executeQuery();

            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var check = new UrlCheck(
                    resultSet.getInt("status_code"),
                    resultSet.getString("title"),
                    resultSet.getString("h1"),
                    resultSet.getString("description"),
                    urlId
                );
                check.setId(resultSet.getInt("id"));
                check.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                result.add(check);
            }
            return result;
        }
    }

    public static Map<Integer, UrlCheck> findLastCheck() throws SQLException {
        var sql = """
         SELECT DISTINCT ON (url_id) *
         FROM url_checks
         ORDER BY created_at DESC""";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var urlIdToCheckAt = new HashMap<Integer, UrlCheck>();
            while (resultSet.next()) {
                var urlId = resultSet.getInt("url_id");
                var check = new UrlCheck(
                    resultSet.getInt("status_code"),
                    resultSet.getString("title"),
                    resultSet.getString("h1"),
                    resultSet.getString("description"),
                    urlId
                );
                check.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                urlIdToCheckAt.put(urlId, check);
            }
            return urlIdToCheckAt;
        }
    }
}


