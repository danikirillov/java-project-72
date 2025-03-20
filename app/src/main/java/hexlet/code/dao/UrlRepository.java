package hexlet.code.dao;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            var createdAt = LocalDateTime.now();
            statement.setString(1, url.getName());
            statement.setTimestamp(2, Timestamp.valueOf(createdAt));
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getInt(1));
                url.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id after saving the entity");
            }
        }
    }

    public static Optional<Url> find(Integer id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var id = resultSet.getInt("id");

                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }


    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                result.add(url);
            }
            return result;
        }
    }
}
