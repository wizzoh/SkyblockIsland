package me.wizzo.skyblockisland.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUI implements InventoryHolder {

    private final Inventory inventory;
    private final String title;
    private final int size;

    public GUI(String title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.title = title;
        this.size = size;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public void closeInventory(Player player) {
        player.closeInventory();
    }
}
