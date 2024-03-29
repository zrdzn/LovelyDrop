package io.github.zrdzn.minecraft.lovelydrop.user;

import com.google.gson.Gson;
import io.github.zrdzn.minecraft.lovelydrop.storage.Storage;
import io.github.zrdzn.minecraft.lovelydrop.user.infra.InMemoryUserSettingRepository;
import io.github.zrdzn.minecraft.lovelydrop.user.infra.PostgresUserSettingRepository;
import io.github.zrdzn.minecraft.lovelydrop.user.infra.SqliteUserSettingRepository;

/**
 * Factory that creates user setting facade instances.
 */
public class UserSettingFacadeFactory {

    private final Storage storage;
    private final Gson gson;

    public UserSettingFacadeFactory(Storage storage, Gson gson) {
        this.storage = storage;
        this.gson = gson;
    }

    /**
     * Creates a new instance of user setting facade.
     *
     * @return the user setting facade instance
     */
    public UserSettingFacade createUserSettingFacade() {
        UserSettingCache userSettingCache = new UserSettingCache();

        switch (this.storage.getType()) {
            case SQLITE:
                return new UserSettingFacade(userSettingCache, new SqliteUserSettingRepository(this.storage.getDataSource(), this.gson));
            case POSTGRESQL:
                return new UserSettingFacade(userSettingCache, new PostgresUserSettingRepository(this.storage.getDataSource(), this.gson));
            case IN_MEMORY:
            default:
                return new UserSettingFacade(userSettingCache, new InMemoryUserSettingRepository());
        }
    }

}
