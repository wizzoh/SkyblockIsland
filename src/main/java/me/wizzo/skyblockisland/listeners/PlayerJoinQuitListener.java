package me.wizzo.skyblockisland.listeners;

import me.wizzo.skyblockisland.SkyblockIsland;
import me.wizzo.skyblockisland.database.PlayerData;
import me.wizzo.skyblockisland.manager.IslandManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final SkyblockIsland main;

    public PlayerJoinQuitListener(SkyblockIsland main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        if (!PlayerData.haveNotIsland(playerName)) {
            String worldName = PlayerData.getIslandName(playerName);
            IslandManager.loadWorld(worldName);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();

        if (!PlayerData.haveNotIsland(playerName)) {
            String worldName = PlayerData.getIslandName(playerName);
            IslandManager.unloadWorld(worldName);
        }
    }
}
