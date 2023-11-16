package me.wizzo.skyblockisland.commands;

import me.wizzo.skyblockisland.database.PlayerData;
import me.wizzo.skyblockisland.manager.GUI;
import me.wizzo.skyblockisland.SkyblockIsland;
import me.wizzo.skyblockisland.manager.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class IslandCommand implements CommandExecutor {

    private final SkyblockIsland main;
    private final String permission;

    public IslandCommand(SkyblockIsland main, String permission) {
        this.main = main;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getConfigString("Only-player-can-execute"));
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission(permission)) {
            sender.sendMessage(main.getConfigString("No-perm"));
            return true;
        }

        String playerName = player.getName();
        if (args.length == 0) {

            if (PlayerData.haveNotIsland(playerName)) {
                IslandManager.createWorld(
                        main.getConfigString("Path.Template-world-copier") + "/",
                        main.getConfigString("Path.Island-folder") + playerName,
                        playerName,
                        playerName
                );

                World world = Bukkit.getWorld(main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(playerName));
                if (world == null) {
                    System.out.println("Errore: mondo non trovato.");
                    return true;
                }
                Location location = new Location(world,
                        world.getSpawnLocation().getX(),
                        world.getSpawnLocation().getY(),
                        world.getSpawnLocation().getZ()
                );
                player.teleport(location);
            } else {
                GUI gui = new GUI(main, main.getConfigString("Gui.Title"), main.getConfigInt("Gui.Size"));
                gui.openInventory(player);
            }
            return true;
        }
        String subCommand = args[0].toLowerCase();

        if (PlayerData.haveNotIsland(playerName)) {
            player.sendMessage(main.getConfigString("Island.Not-have-island"));
            return true;
        }

        switch (subCommand) {
            case "teleport":
            case "tp":
                islandTeleport(player, main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(player.getName()));
                player.sendMessage(main.getConfigString("Island.Teleport-success"));
                break;

            case "reset":
                islandTeleport(player, main.getConfigString("Path.Template-world-copier"));
                IslandManager.resetWorld(
                        main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(playerName),
                        main.getConfigString("Path.Template-world-copier") + "/",
                        main.getConfigString("Path.Island-folder") + playerName,
                        playerName,
                        playerName
                );
                islandTeleport(player, main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(player.getName()));
                player.sendMessage(main.getConfigString("Island.Reset-success"));
                break;

            case "delete":
                World defWorld = Bukkit.getWorld(main.getConfigString("Path.Template-world-copier"));
                if (defWorld == null) {
                    System.out.println("Errore: mondo non trovato.");
                    return true;
                }
                Location location1 = new Location(defWorld,
                        defWorld.getSpawnLocation().getX(),
                        defWorld.getSpawnLocation().getY(),
                        defWorld.getSpawnLocation().getZ()
                );
                player.teleport(location1);
                String islandFolderName = main.getConfigString("Path.Island-folder") + PlayerData.getIslandName(playerName);

                IslandManager.unloadWorld(islandFolderName, false);
                IslandManager.deleteWorld(new File(islandFolderName));
                PlayerData.removeIsland(playerName);
                player.sendMessage(main.getConfigString("Island.Delete-success"));
                break;
        }
        return true;
    }

    private void islandTeleport(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            System.out.println("Errore: mondo non trovato.");
            return;
        }
        Location location = new Location(world,
                world.getSpawnLocation().getX(),
                world.getSpawnLocation().getY(),
                world.getSpawnLocation().getZ()
        );
        player.teleport(location);
    }
}
