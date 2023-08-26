package me.fortibrine.postget.command;

import me.fortibrine.postget.util.vk.VkPostsHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.fortibrine.postget.main.Main;

public class CommandGet implements CommandExecutor {

    private Main plugin;
    public CommandGet(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command allowed only for players!");
            return true;
        }

        Player player = (Player) sender;
        VkPostsHolder holder = plugin.getPostsHolder();

        if (holder.hasPosts()) holder.getLastPost().openBook(player);
        else player.sendMessage(plugin.getMessageConfigUtil().parseMessage("not-found"));

        return true;
    }

}
