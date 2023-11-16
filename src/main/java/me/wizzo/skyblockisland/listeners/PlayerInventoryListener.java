package me.wizzo.skyblockisland.listeners;

import me.wizzo.skyblockisland.SkyblockIsland;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryListener implements Listener {

    private final SkyblockIsland main;

    public PlayerInventoryListener(SkyblockIsland main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(main.getConfigString("Gui.Title"))) {
            ItemStack itemClicked = event.getCurrentItem();

            if (itemClicked != null) {
                Player player = (Player) event.getWhoClicked();
                if (itemClicked.getType() == getMaterialFromConfig(main.getConfigString("Gui.Items.Teleport-item"))
                        && itemClicked.getItemMeta().hasDisplayName()
                        && itemClicked.getItemMeta().getDisplayName().equals(main.getConfigString("Gui.Items.Teleport-name"))) {
                    player.performCommand("island teleport");

                } else if (itemClicked.getType() == getMaterialFromConfig(main.getConfigString("Gui.Items.Delete-item"))
                        && itemClicked.getItemMeta().hasDisplayName()
                        && itemClicked.getItemMeta().getDisplayName().equals(main.getConfigString("Gui.Items.Delete-name"))) {
                    player.performCommand("island delete");

                } else if (itemClicked.getType() == getMaterialFromConfig(main.getConfigString("Gui.Items.Reset-item"))
                        && itemClicked.getItemMeta().hasDisplayName()
                        && itemClicked.getItemMeta().getDisplayName().equals(main.getConfigString("Gui.Items.Reset-name"))) {
                    player.performCommand("island reset");
                }
            }
            event.setCancelled(true);
        }
    }

    private Material getMaterialFromConfig(String string) {
        return Material.matchMaterial(string.toUpperCase());
    }
}
