package me.fortibrine.postget.util.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallpostFull;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import me.fortibrine.postget.main.Main;

import java.util.List;

public class VkPostsHolder {

    @Getter
    private VkPost lastPost;

    private final int groupId;
    private final VkApiClient client;
    private final ServiceActor actor;

    public VkPostsHolder(Main plugin, ConfigurationSection config) {
        ConfigurationSection vkConfig = config.getConfigurationSection("application");

        groupId = vkConfig.getInt("group-id");

        client = new VkApiClient(new HttpTransportClient());
        actor = new ServiceActor(vkConfig.getInt("application-id"), vkConfig.getString("application-token"));

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::fetchPosts, 0, config.getInt("period") * 20L);
    }

    @SneakyThrows
    private void fetchPosts() {
        List<WallpostFull> wall = client.wall()
                .get(actor).ownerId(groupId)
                .count(1).offset(1)
                .execute().getItems();

        if (!wall.isEmpty()) {
            WallpostFull rawPost = wall.get(0);
            lastPost = new VkPost(rawPost.getId(), rawPost.getText());
        }
    }

    public boolean hasPosts() {
        return lastPost != null;
    }

}
