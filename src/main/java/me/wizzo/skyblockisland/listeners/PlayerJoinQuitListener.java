package me.wizzo.skyblockisland.listeners;

import me.wizzo.skyblockisland.SkyblockIsland;
import me.wizzo.skyblockisland.database.PlayerData;
import me.wizzo.skyblockisland.manager.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();
        World world = Bukkit.getWorld(main.getConfigString("Path.Template-world-copier").replace("/", ""));

        if (world == null) {
            System.out.println("Mondo non trovato.");
            return;
        }
        player.teleport(new Location(
                world,
                world.getSpawnLocation().getX(),
                world.getSpawnLocation().getY(),
                world.getSpawnLocation().getZ()
        ));

        if (!PlayerData.haveNotIsland(playerName)) {
            String worldName = main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(playerName);
            IslandManager.loadWorld(worldName);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();

        if (!PlayerData.haveNotIsland(playerName)) {
            String worldName = main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(playerName);
            IslandManager.unloadWorld(worldName);
        }
    }
}
