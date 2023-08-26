package me.fortibrine.postget.listeners;

import me.fortibrine.postget.util.vk.VkPost;
import me.fortibrine.postget.util.vk.VkPostsHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import me.fortibrine.postget.main.Main;

public class PlayerJoinListener implements Listener {

    private Main plugin;
    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        VkPostsHolder holder = plugin.getPostsHolder();

        if (holder.hasPosts()) Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            VkPost post = holder.getLastPost();

            if (!post.isViewedBy(player))
                post.openBook(player);
        }, 15);
    }

}
