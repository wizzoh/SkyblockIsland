package me.wizzo.skyblockisland.commands;

import me.wizzo.skyblockisland.manager.GUI;
import me.wizzo.skyblockisland.SkyblockIsland;
import me.wizzo.skyblockisland.manager.IslandManager;
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

        if (args.length == 0) {
            GUI gui = new GUI(main.getConfigString("Gui.Title"), main.getConfigInt("Gui.Size"));
            gui.openInventory(player);
            return true;
        }
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "teleport":
            case "tp":
                //do somethings
                break;

            case "regen":
            case "reset":
                IslandManager.resetWorld(
                        new File(main.getConfigString("Path.Island-folder")),
                        main.getConfigString("Path.Template-world-copier"),
                        main.getConfigString("Path.Island-folder")
                );
                break;

            case "delete":
                IslandManager.deleteWorld(new File(main.getConfigString("Path.Island-folder"))); // + worldName from database
                break;
        }
        return true;
    }
}
