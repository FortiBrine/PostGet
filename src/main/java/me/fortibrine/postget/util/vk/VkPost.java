package me.fortibrine.postget.util.vk;

import me.fortibrine.postget.main.Main;
import me.fortibrine.postget.util.BookUtil;
import me.fortibrine.postget.util.sql.PostViewHistory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class VkPost {

    private final int id;
    private final String content;
    private final PostViewHistory viewHistory;
    private final ItemStack book;
    private final BookUtil bookUtil;

    public VkPost(int id, String content) {
        this.id = id;
        this.content = content;

        this.viewHistory = Main.getMain().getViewHistory();
        this.bookUtil = Main.getMain().getBookUtil();

        book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.addPage(content);

        book.setItemMeta(meta);
    }

    public void openBook(Player player) {
        this.bookUtil.openBook(player, book);

        this.viewHistory.updateLastPostView(player, id);
    }

    public boolean isViewedBy(Player player) {
        return viewHistory.lookupLastPostView(player) == id;
    }

}
