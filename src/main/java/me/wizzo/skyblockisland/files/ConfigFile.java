package me.wizzo.skyblockisland.files;

import me.wizzo.skyblockisland.SkyblockIsland;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConfigFile {

    private File file;
    private FileConfiguration config;
    private final SkyblockIsland main;
    private String error;

    public ConfigFile(SkyblockIsland main) {
        this.main = main;
        setup("config.yml");
    }

    public void setup(String fileName) {
        file = new File(main.getDataFolder(), fileName);
        error = main.colorsMessage("&cBedWars: Errore durante il caricamento dei config");

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
        config.set("Prefix", "&6&lIsland &r");
        config.set("No-perm", "{prefix}&7Non hai il permesso per eseguire questo comando.");
        config.set("Only-player-can-execute", "{prefix}&7Solo i player possono eseguire questo comando.");

        config.set("Gui.Title", "&eGestore isola");
        config.set("Gui.Size", 9);

        config.set("Gui.Items.Teleport-item", "redstone_torch");
        config.set("Gui.Items.Teleport-slot", 1);
        config.set("Gui.Items.Teleport-name", "&aVai alla tua isola");
        config.set("Gui.Items.Teleport-lore", Arrays.asList("&7Clicca qui per tornare", "&7alla tua isola."));

        config.set("Gui.Items.Delete-item", "clock");
        config.set("Gui.Items.Delete-slot", 4);
        config.set("Gui.Items.Delete-name", "&cCancella l'isola");
        config.set("Gui.Items.Delete-lore", Arrays.asList("&7Clicca qui per &ccancellare", "&7la tua isola.", "&7Ricordati che non potrai", "&7tornare indietro."));

        config.set("Gui.Items.Reset-item", "barrier");
        config.set("Gui.Items.Reset-slot", 7);
        config.set("Gui.Items.Reset-name", "&cResetta l'isola");
        config.set("Gui.Items.Reset-lore",
                Arrays.asList("&7Clicca qui per &cresettare",
                "&7la tua isola.", "&7Ricordati che non potrai", "&7tornare indietro.",
                "&7Prezzo: &6500 coins"
                )
        );

        config.setComments("test", Arrays.asList("aaaa", "testet"));
        config.set("Path.Template-world-copier", "world/");
        config.set("Path.Island-folder", "islands/");

        config.set("Island.Not-have-island", "{prefix}&7Devi prima creare un'isola con &e/island");
        config.set("Island.Teleport-success", "{prefix}&7Sei stato teletrasportato alla tua isola.");
        config.set("Island.Reset-success", "{prefix}&7L'isola è stata resettata correttamente.");
        config.set("Island.Delete-success", "{prefix}&7L'isola è stata cancellata correttamente.");
    }
}
