package me.wizzo.skyblockisland.files;

import me.wizzo.skyblockisland.SkyblockIsland;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DatabaseFile {

    private File file;
    private FileConfiguration config;
    private final SkyblockIsland main;
    private String error;

    public DatabaseFile(SkyblockIsland main) {
        this.main = main;
        setup("database.yml");
    }

    public void setup(String fileName) {
        file = new File(main.getDataFolder(), fileName);
        error = main.colorsMessage("&cBedWars: Errore durante il caricamento del database");

        if (!file.exists()) {
            try {
                boolean success = file.createNewFile();
                if (success) {
                    load();
                    defaultConfig();
                    save();
                } else {
                    main.getConsole().sendMessage(error);
                }
            } catch (IOException e) {
                main.getConsole().sendMessage(error);
                e.printStackTrace();
            }
        } else {
            load();
            save();
        }
    }

    public void load() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            main.getConsole().sendMessage(error);
        }
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            main.getConsole().sendMessage(error);
        }
    }

    private void defaultConfig() {
        config.set("Url", "jdbc:mysql://{ip}:{port}/{database}");
        config.set("ClassName", "com.mysql.jdbc.Driver");
        config.set("Host", "127.0.0.1");
        config.set("Port", 3306);
        config.set("Database", "database");
        config.set("Username", "root");
        config.set("Password", "password");
        config.set("Max-pool-size", 10);
        config.set("Tables.Player-data", "player_island");
    }
}
