package io.github.zrdzn.minecraft.lovelydrop.storage;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class StorageConfig extends OkaeriConfig {

    @Comment("A type of the data source that should be used. Available: SQLITE, POSTGRESQL, IN_MEMORY")
    public StorageType type = StorageType.SQLITE;

    @Comment("")
    @Comment("A file name if SQLite is used.")
    public String sqliteFile = "lovelydrop.db";

    @Comment("")
    @Comment("Database host address.")
    public String host = "localhost";

    @Comment("")
    @Comment("Database port.")
    public int port = 5432;

    @Comment("")
    @Comment("Database name.")
    public String database = "lovelydrop";

    @Comment("")
    @Comment("Database user name.")
    public String user = "minecraft_user";

    @Comment("")
    @Comment("Database password.")
    public String password = "tomatoes";

    @Comment("")
    @Comment("Should SSL be enabled for database?")
    public boolean enableSsl = false;

    @Comment("")
    @Comment("Database maximum pool size.")
    public int maximumPoolSize = 10;

    @Comment("")
    @Comment("Database connection timeout.")
    public int connectionTimeout = 5000;

}
