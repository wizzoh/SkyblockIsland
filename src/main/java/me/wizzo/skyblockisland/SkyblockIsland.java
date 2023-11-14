package me.wizzo.skyblockisland;

import me.wizzo.skyblockisland.files.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyblockIsland extends JavaPlugin {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private String prefix;
    private ConfigFile configFile;

    @Override
    public void onEnable() {
        createFolder();
        //Island command
        files();
        //Listeners
        this.prefix = configFile.get().getString("Prefix");

    }

    @Override
    public void onDisable() {
    }

    private void createFolder() {
        if (!getDataFolder().exists()) {
            boolean success = getDataFolder().mkdirs();
            if (!success) {
                console.sendMessage(colorsMessage(prefix + "&cImpossibile creare la cartella, disabilitazione plugin in corso.."));
                getPluginLoader().disablePlugin(this);
            }
        }
    }

    private void files() {
        this.configFile = new ConfigFile(this);
    }

    public String colorsMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }
}
