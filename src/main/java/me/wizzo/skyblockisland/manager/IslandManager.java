package me.wizzo.skyblockisland.manager;

import me.wizzo.skyblockisland.database.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class IslandManager {

    public static void createWorld(String sourceFolderName, String destinationFolderName, String playerName, String worldName) {
        File source = null;
        
        try {
            source = new File(Bukkit.getServer().getWorldContainer().getPath() + "\\" + sourceFolderName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (source != null) {
           File destination = new File(Bukkit.getServer().getWorldContainer().getPath() + "\\" + destinationFolderName);

           try {
               copyFolder(source.toPath(), destination.toPath());
           } catch (IOException e) {
               e.printStackTrace();
           }
        }
        PlayerData.createIslandPlayer(playerName, worldName);
        loadWorld(destinationFolderName);
    }

    public static void deleteWorld(File worldFolder) {
        unloadWorld(worldFolder.getName());
        if (worldFolder.exists()) {
            File[] files = worldFolder.listFiles();
            if (files != null){
                for (File file: files) {
                    if (file.isDirectory()) {
                        deleteWorld(file);
                    } else {
                        file.delete();
                    }
                }
            }
            worldFolder.delete();
        }
    }

    public static void resetWorld(File worldFile, String sourceFolderName, String destinationFolderName, String playerName, String worldName) {
        deleteWorld(worldFile);
        createWorld(sourceFolderName, destinationFolderName, playerName, worldName);
    }

    public static void unloadWorld(String worldName) {
        Bukkit.getServer().unloadWorld(worldName, true);
    }

    public static void loadWorld(String worldName) {
        Bukkit.getServer().createWorld(new WorldCreator(worldName));
    }

    private static void copyFolder(Path source, Path destination) throws IOException {
        Files.walk(source).forEach(sourcePath -> {
            try {
                Path destinationPath = destination.resolve(source.relativize(sourcePath));

                if (Files.isDirectory(sourcePath)) {
                    Files.createDirectories(destinationPath);
                } else {
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    if (destinationPath.toFile().getName().equalsIgnoreCase("uid.dat")) {
                        boolean success = destinationPath.toFile().delete();
                        if (!success) {
                            System.out.println("Errore nel copiare i dati.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
