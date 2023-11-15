package me.wizzo.skyblockisland.manager;

import me.wizzo.skyblockisland.SkyblockIsland;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI implements InventoryHolder {

    private final SkyblockIsland main;
    private final Inventory inventory;

    public GUI(SkyblockIsland main, String title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.main = main;
        addItemToInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    private void addItemToInventory() {

        // Item per il teleport
        ItemStack teleportItem = new ItemStack(
                Material.valueOf(main.getConfigString("Gui.Items.Teleport-item").toUpperCase())
        );
        ItemMeta tpMeta = teleportItem.getItemMeta();
        tpMeta.setDisplayName(main.getConfigString("Gui.Items.Teleport-name"));
        tpMeta.setLore(main.getConfigStringList("Gui.Items.Teleport-lore"));
        teleportItem.setItemMeta(tpMeta);

        // Item per il delete
        ItemStack deleteItem = new ItemStack(
                Material.valueOf(main.getConfigString("Gui.Items.Delete-item").toUpperCase())
        );
        ItemMeta deleteMeta = teleportItem.getItemMeta();
        deleteMeta.setDisplayName(main.getConfigString("Gui.Items.Delete-name"));
        deleteMeta.setLore(main.getConfigStringList("Gui.Items.Delete-lore"));
        teleportItem.setItemMeta(deleteMeta);

        // Item per il reset
        ItemStack resetItem = new ItemStack(
                Material.valueOf(main.getConfigString("Gui.Items.Reset-item").toUpperCase())
        );
        ItemMeta resetMeta = teleportItem.getItemMeta();
        resetMeta.setDisplayName(main.getConfigString("Gui.Items.Reset-name"));
        resetMeta.setLore(main.getConfigStringList("Gui.Items.Reset-lore"));
        teleportItem.setItemMeta(resetMeta);

        inventory.setItem(main.getConfigInt("Gui.Items.Teleport-slot"), teleportItem);
        inventory.setItem(main.getConfigInt("Gui.Items.Delete-slot"), deleteItem);
        inventory.setItem(main.getConfigInt("Gui.Items.Reset-slot"), resetItem);
    }
}
