package me.wizzo.skyblockisland;

import me.wizzo.skyblockisland.commands.IslandCommand;
import me.wizzo.skyblockisland.database.HikariCPSettings;
import me.wizzo.skyblockisland.files.ConfigFile;
import me.wizzo.skyblockisland.files.DatabaseFile;
import me.wizzo.skyblockisland.listeners.PlayerJoinQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class SkyblockIsland extends JavaPlugin {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private String prefix = "";
    private ConfigFile configFile;
    private DatabaseFile databaseFile;
    private HikariCPSettings hikariCPSettings;

    @Override
    public void onEnable() {
        createFolders();
        files();
        database();
        getCommand("island").setExecutor(new IslandCommand(this, "skyblockisland.island"));
        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        this.prefix = configFile.get().getString("Prefix");

        try {
            hikariCPSettings.initSource(this);
            console.sendMessage("");
            console.sendMessage(colorsMessage(this.getDescription().getName() + " &7Connessione al database eseguita!"));
            console.sendMessage(colorsMessage(this.getDescription().getName() + " &7Plugin online - versione " + this.getDescription().getVersion()));
            console.sendMessage(colorsMessage(this.getDescription().getName() + " &7By wizzo"));
            console.sendMessage("");
        } catch (Exception e) {
            console.sendMessage(colorsMessage(this.getDescription().getName() + " &7Connessione al database non riuscita!"));
            console.sendMessage(colorsMessage(this.getDescription().getName() + " &7Spegnimento in corso.."));
            e.printStackTrace();
            this.getPluginLoader().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        if (hikariCPSettings.getSource() != null) {
            hikariCPSettings.close(hikariCPSettings.getSource());
        }
        console.sendMessage("");
        console.sendMessage(colorsMessage(this.getDescription().getName() + " &7Plugin offline - versione " + this.getDescription().getVersion()));
        console.sendMessage(colorsMessage(this.getDescription().getName() + " stop&7By wizzo"));
        console.sendMessage("");
    }

    //---------------

    private void createFolders() {
        if (!getDataFolder().exists()) {
            boolean success = getDataFolder().mkdirs();
            if (!success) {
                console.sendMessage(colorsMessage(prefix + "&cImpossibile creare la cartella per i config, disabilitazione plugin in corso.."));
                getPluginLoader().disablePlugin(this);
            }
        }
    }

    private void files() {
        this.configFile = new ConfigFile(this);
        this.databaseFile = new DatabaseFile(this);

        File islandFolder = new File(System.getProperty("user.dir") + File.separator + configFile.get().getString("Path.Island-folder"));
        if (!islandFolder.exists()) {
            boolean success = islandFolder.mkdirs();
            if (!success) {
                console.sendMessage(colorsMessage(prefix + "&cImpossibile creare la cartella per le isole, disabilitazione plugin in corso.."));
                getPluginLoader().disablePlugin(this);
            }
        }
    }

    private void database() {
        this.hikariCPSettings = new HikariCPSettings();
    }

    //---------------

    public String colorsMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public ConsoleCommandSender getConsole() {
        return console;
    }
    public String getConfigString(String string) {
        return colorsMessage(configFile.get().getString(string)
                .replace("{prefix}", prefix)
        );
    }
    public int getConfigInt(String string) {
        return configFile.get().getInt(string);
    }
    public DatabaseFile getDatabaseConfig() {
        return databaseFile;
    }
    public HikariCPSettings getHikariCPSettings() {
        return this.hikariCPSettings;
    }
}
