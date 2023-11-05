package io.github.zrdzn.minecraft.lovelydrop.storage;

import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;

/**
 * Class that represents storage by holding data source instance with associated storage type.
 * Some storage types may not have data source instance, such as in-memory storage, therefore data source instance is nullable.
 */
public class Storage {

    private final DataSource dataSource;
    private final StorageType type;

    public Storage(DataSource dataSource, StorageType type) {
        this.dataSource = dataSource;
        this.type = type;
    }

    @Nullable
    public DataSource getDataSource() {
        return this.dataSource;
    }

    public StorageType getType() {
        return this.type;
    }

}
