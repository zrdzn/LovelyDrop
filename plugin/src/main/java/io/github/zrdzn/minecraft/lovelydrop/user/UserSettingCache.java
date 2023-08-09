package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserSettingCache {

    private final Map<UUID, UserSetting> usersSettings;

    public UserSettingCache() {
        this.usersSettings = new ConcurrentHashMap<>();
    }

    UserSetting addUserSetting(UserSetting userSetting) {
        return this.usersSettings.put(userSetting.getPlayerId(), userSetting);
    }

    Optional<UserSetting> findUserSettingByPlayerId(UUID playerId) {
        return Optional.ofNullable(this.usersSettings.get(playerId));
    }

    Set<UserSetting> findAllUserSettings() {
        return Collections.unmodifiableSet(new HashSet<>(this.usersSettings.values()));
    }

    void removeUserSettingByPlayerId(UUID playerId) {
        this.usersSettings.remove(playerId);
    }

}
