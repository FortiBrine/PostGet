package me.fortibrine.postget.util.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

@Getter
public class MySQLConnectionUtil {

    private final Connection connection;

    public MySQLConnectionUtil(ConfigurationSection config) {
        config = config.getConfigurationSection("database");

        connection = createConnection(
                String.format("jdbc:mysql://%s/%s", config.getString("host"), config.getString("name")),
                config.getString("user"), config.getString("password")
        );
    }

    @SneakyThrows
    public Connection createConnection(String url, String user, String password) {
        HikariConfig config = createHikariConfig(url, user, password);
        HikariDataSource dataSource = new HikariDataSource(config);

        return dataSource.getConnection();
    }

    public HikariConfig createHikariConfig(String url, String user, String password) {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(url);

        config.setUsername(user);
        config.setPassword(password);

        return config;
    }

    @SneakyThrows
    public ResultSet query(String query, Object... params) {
        PreparedStatement statement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

        return statement.executeQuery();
    }

    @SneakyThrows
    public int update(String query, Object... params) {
        PreparedStatement statement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

        return statement.executeUpdate();
    }

    @SneakyThrows
    public CompletableFuture<Integer> updateAsync(String query, Object... params) {
        return CompletableFuture.supplyAsync(() -> update(query, params));
    }

}
