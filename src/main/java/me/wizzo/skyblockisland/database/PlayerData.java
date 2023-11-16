package me.wizzo.skyblockisland.database;

import me.wizzo.skyblockisland.SkyblockIsland;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerData {

    private static SkyblockIsland main;
    private static String formatted(String string) {
        return string.replace("{table}", main.getHikariCPSettings().playerDataTable);
    }

    public PlayerData(SkyblockIsland main) {
        PlayerData.main = main;
    }

    public static void createTables() {
        String query = formatted("create table if not exists {table} (Player_name varchar(18) not null, Island_name varchar(18) not null, primary key (Player_name))");

        try (Connection connection = main.getHikariCPSettings().getSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createIslandPlayer(String playerName, String worldName) {
        String query = formatted("replace into {table} (Player_name, Island_name) values(?,?)");

        try (Connection connection = main.getHikariCPSettings().getSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerName);
            ps.setString(2, worldName);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean haveNotIsland(String playerName) {
        String query = formatted("select * from {table} where Player_name=?");

        try (Connection connection = main.getHikariCPSettings().getSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerName);
            ResultSet results = ps.executeQuery();

            return !results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public static String getIslandName(String playerName) {
        String query = formatted("select Island_name from {table} where Player_name=?");

        try (Connection connection = main.getHikariCPSettings().getSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerName);
            ResultSet results = ps.executeQuery();

            if (results.next()) {
                return results.getString("Island_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeIsland(String playerName) {
        String query = formatted("delete from {table} where Player_name=?");

        try (Connection connection = main.getHikariCPSettings().getSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
