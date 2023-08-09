package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserSettingRepository implements UserSettingRepository {

    private final Map<UUID, UserSetting> usersSettings;

    public InMemoryUserSettingRepository() {
        this.usersSettings = new ConcurrentHashMap<>();
    }

    @Override
    public UserSetting createUserSetting(UUID playerId, Set<String> disabledDrops, Map<String, Boolean> dropsToInventory) {
        return this.usersSettings.put(playerId, new UserSetting(playerId, disabledDrops, dropsToInventory));
    }

    @Override
    public Optional<UserSetting> findUserSettingByPlayerId(UUID playerId) {
        return Optional.ofNullable(this.usersSettings.get(playerId));
    }

}
