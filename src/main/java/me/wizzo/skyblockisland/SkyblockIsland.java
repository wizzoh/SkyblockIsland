package me.wizzo.skyblockisland;

import me.wizzo.skyblockisland.commands.IslandCommand;
import me.wizzo.skyblockisland.database.HikariCPSettings;
import me.wizzo.skyblockisland.database.PlayerData;
import me.wizzo.skyblockisland.files.ConfigFile;
import me.wizzo.skyblockisland.files.DatabaseFile;
import me.wizzo.skyblockisland.listeners.PlayerInventoryListener;
import me.wizzo.skyblockisland.listeners.PlayerJoinQuitListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class SkyblockIsland extends JavaPlugin {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private String prefix = "";
    private ConfigFile configFile;
    private DatabaseFile databaseFile;
    private HikariCPSettings hikariCPSettings;
    private static Economy economy;

    @Override
    public void onEnable() {
        createFolders();
        files();
        database();
        getCommand("island").setExecutor(new IslandCommand(this, "skyblockisland.island"));
        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(this), this);
        this.prefix = configFile.get().getString("Prefix");

        if (!setupEconomy()) {
            System.out.println("No vault found");
            getPluginLoader().disablePlugin(this);
        }

        try {
            hikariCPSettings.initSource(this);
            PlayerData.createTables();
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
        new PlayerData(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            System.out.println("Economy vault nullo");
            return false;
        }
        economy = rsp.getProvider();
        System.out.println("Economy vault trovato");
        return true;
    }

    //---------------

    public String colorsMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public List<String> colorsMessage(List<String> message) {
        List<String> list = new ArrayList<>();

        for (String string: message) {
            list.add(ChatColor.translateAlternateColorCodes('&', string.replace("{cost}",
                    String.valueOf(getConfigInt("Island.Reset-cost"))))
            );
        }
        return list;
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
    public List<String> getConfigStringList(String string) {
        return colorsMessage(configFile.get().getStringList(string));
    }
    public DatabaseFile getDatabaseConfig() {
        return databaseFile;
    }
    public HikariCPSettings getHikariCPSettings() {
        return this.hikariCPSettings;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
