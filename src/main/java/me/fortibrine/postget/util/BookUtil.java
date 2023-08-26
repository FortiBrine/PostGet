package me.fortibrine.postget.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookUtil {

    private ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public void openBook(Player player, ItemStack itemStack) {
        final int slot = player.getInventory().getHeldItemSlot();
        final ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, itemStack);

        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.OPEN_BOOK);
        packetContainer.getIntegers().write(0, slot);
        protocolManager.sendServerPacket(player, packetContainer);
        player.getInventory().setItem(slot, old);
    }

}
