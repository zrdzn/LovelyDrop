package io.github.zrdzn.minecraft.lovelydrop.storage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.google.common.io.Files;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class that creates storage instance based on configuration.
 */
public class StorageFactory {

    private final Logger logger = LoggerFactory.getLogger(StorageFactory.class);

    private final StorageConfig config;
    private final File dataFolder;

    public StorageFactory(StorageConfig config, File dataFolder) {
        this.config = config;
        this.dataFolder = dataFolder;
    }

    /**
     * Creates storage instance based on configuration.
     *
     * @return the storage instance
     * @throws StorageException if storage instance cannot be created
     */
    public Storage createStorage() {
        HikariDataSource dataSource;
        StorageType storageType;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername(this.config.getUser());
        hikariConfig.setPassword(this.config.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", true);
        hikariConfig.addDataSourceProperty("tcpKeepAlive", true);
        hikariConfig.setLeakDetectionThreshold(60000L);
        hikariConfig.setMaximumPoolSize(this.config.getMaximumPoolSize());
        hikariConfig.setConnectionTimeout(this.config.getConnectionTimeout());
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setIdleTimeout(30000L);

        String host = this.config.getHost();
        int port = this.config.getPort();
        String database = this.config.getDatabase();

        switch (this.config.getType()) {
            case SQLITE:
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException exception) {
                    throw new StorageException("SQLite driver not found.", exception);
                }

                File sqliteFile = new File(this.dataFolder, this.config.getSqliteFile());
                if (!sqliteFile.exists()) {
                    try {
                        Files.createParentDirs(sqliteFile);

                        sqliteFile.createNewFile();
                    } catch (IOException exception) {
                        throw new StorageException("Could not create SQLite file.", exception);
                    }
                }

                hikariConfig.setJdbcUrl("jdbc:sqlite:" + sqliteFile.getAbsolutePath());

                try {
                    dataSource = new HikariDataSource(hikariConfig);
                    this.logger.info("Choosing SQLite as a storage provider.");
                } catch (Exception exception) {
                    throw new StorageException("Could not open connection to SQLite file.", exception);
                }

                String sqliteCreateEliminationTableQuery =
                        "CREATE TABLE IF NOT EXISTS users_settings (" +
                                "   player_id VARCHAR(36) PRIMARY KEY," +
                                "   disabled_drops JSON NOT NULL," +
                                "   drops_to_inventory JSON NOT NULL" +
                                ");";
                this.applySchema(dataSource, sqliteCreateEliminationTableQuery);

                storageType = StorageType.SQLITE;

                break;
            case POSTGRESQL:
                boolean enableSsl = this.config.isEnableSsl();
                if (!enableSsl) {
                    this.logger.warn("Storage connection is configured without SSL enabled.");
                }

                hikariConfig.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s?ssl=%s", host, port, database, enableSsl));

                try {
                    dataSource = new HikariDataSource(hikariConfig);
                    this.logger.info("Choosing PostgreSQL as a storage provider.");
                } catch (Exception exception) {
                    throw new StorageException("Could not open connection to PostgreSQL database.", exception);
                }

                String postgresCreateEliminationTableQuery =
                        "CREATE TABLE IF NOT EXISTS users_settings (" +
                                "   player_id VARCHAR(36) PRIMARY KEY," +
                                "   disabled_drops JSON NOT NULL," +
                                "   drops_to_inventory JSON NOT NULL" +
                                ");";
                this.applySchema(dataSource, postgresCreateEliminationTableQuery);

                storageType = StorageType.POSTGRESQL;

                break;
            case IN_MEMORY:
            default:
                throw new IllegalArgumentException("There is no such storage type.");
        }

        return new Storage(dataSource, storageType);
    }

    private void applySchema(DataSource dataSource, String schemaCreateQuery) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(schemaCreateQuery)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new StorageException("Could not create table.", exception);
        }
    }

}
