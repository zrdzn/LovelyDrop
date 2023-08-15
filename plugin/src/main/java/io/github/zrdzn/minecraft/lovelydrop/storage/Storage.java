package io.github.zrdzn.minecraft.lovelydrop.storage;

import javax.sql.DataSource;

/**
 * Class that represents storage by holding data source instance with associated storage type.
 */
public class Storage {

    private final DataSource dataSource;
    private final StorageType type;

    public Storage(DataSource dataSource, StorageType type) {
        this.dataSource = dataSource;
        this.type = type;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public StorageType getType() {
        return this.type;
    }

}
