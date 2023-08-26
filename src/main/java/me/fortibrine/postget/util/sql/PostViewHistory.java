package me.fortibrine.postget.util.sql;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

public class PostViewHistory {

    private final MySQLConnectionUtil database;
    private final LoadingCache<String, Integer> historyCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Integer>() {
                @Override
                @SneakyThrows
                public Integer load(String player) throws Exception {
                    try (ResultSet rs = database.query(
                            "SELECT last_post_id FROM posts_view_history WHERE player = ?", player)) {
                        return rs.next() ? rs.getInt("last_post_id") : -1;
                    }
                }
            });

    public PostViewHistory(Configuration config) {
        database = new MySQLConnectionUtil(config);

        database.update(//language=mysql
                "CREATE TABLE IF NOT EXISTS posts_view_history(player VARCHAR(16) NOT NULL, last_post_id INT NOT NULL, PRIMARY KEY (player))"
        );
    }

    public int lookupLastPostView(Player player) {
        return lookupLastPostView(player.getName());
    }

    public int lookupLastPostView(String player) {
        try {
            return historyCache.get(player);
        } catch (Throwable t) {
            t.printStackTrace();
            return -1;
        }
    }

    public void updateLastPostView(Player player, int newsId) {
        updateLastPostView(player.getName(), newsId);
    }

    public void updateLastPostView(String player, int newsId) {
        historyCache.put(player, newsId);
        database.updateAsync(
                "INSERT INTO posts_view_history VALUES (?, ?) ON DUPLICATE KEY UPDATE `last_post_id` = ?;",
                player, newsId, newsId
        );
    }

}
