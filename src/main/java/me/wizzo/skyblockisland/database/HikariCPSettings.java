package me.wizzo.skyblockisland.database;

import com.zaxxer.hikari.HikariDataSource;
import me.wizzo.skyblockisland.SkyblockIsland;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPSettings {

    public String className, url, host, port, database, username, password, playerDataTable;
    HikariDataSource dataSource;

    public void initSource(SkyblockIsland main) throws SQLException {
        FileConfiguration dbFile = main.getDatabaseConfig().get();

        this.className = dbFile.getString("ClassName");
        this.host = dbFile.getString("Host");
        this.port = dbFile.getString("Port");
        this.database = dbFile.getString("Database");
        this.username = dbFile.getString("Username");
        this.password = dbFile.getString("Password");
        this.playerDataTable = dbFile.getString("Tables.Player-data");
        this.url = dbFile.getString("Url")
                .replace("{ip}", host)
                .replace("{port}", port)
                .replace("{database}", database);

        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(dbFile.getInt("Max-pool-size"));
        dataSource.setDriverClassName(className);
        dataSource.setJdbcUrl(url);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
        dataSource.addDataSourceProperty("database", database);

        testDataSource(dataSource);
    }

    public void close(HikariDataSource source) {
        source.close();
    }

    public HikariDataSource getSource() {
        return dataSource;
    }

    private void testDataSource(DataSource source) throws SQLException {
        try (Connection connection = source.getConnection()) {
            if (!connection.isValid(1000)) {
                throw new SQLException("Impossibile eseguire la connessione al database");
            }
        }
    }
}
