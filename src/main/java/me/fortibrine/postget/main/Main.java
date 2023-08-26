package me.fortibrine.postget.main;

import lombok.Getter;
import me.fortibrine.postget.command.CommandGet;
import me.fortibrine.postget.util.MessageConfigUtil;
import me.fortibrine.postget.util.sql.PostViewHistory;
import me.fortibrine.postget.listeners.PlayerJoinListener;
import me.fortibrine.postget.util.BookUtil;
import me.fortibrine.postget.util.vk.VkPostsHolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class Main extends JavaPlugin {

    private VkPostsHolder postsHolder;
    private PostViewHistory viewHistory;
    private BookUtil bookUtil = new BookUtil();
    private MessageConfigUtil messageConfigUtil;

    private static Main INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        File configFile = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
        FileConfiguration config = this.getConfig();

        postsHolder = new VkPostsHolder(this, config);
        viewHistory = new PostViewHistory(config);
        messageConfigUtil = new MessageConfigUtil(this);

        getCommand("get").setExecutor(new CommandGet(this));
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    public static Main getMain() {
        return INSTANCE;
    }

}
