package io.github.zrdzn.minecraft.lovelydrop.user.infra;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSetting;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingRepository;

public class InMemoryUserSettingRepository implements UserSettingRepository {

    private final Map<UUID, UserSetting> usersSettings;

    public InMemoryUserSettingRepository() {
        this.usersSettings = new ConcurrentHashMap<>();
    }

    @Override
    public void createOrUpdateUserSettings(List<UserSetting> userSettings) {
        userSettings.forEach(userSetting -> this.usersSettings.put(userSetting.getPlayerId(), userSetting));
    }

    @Override
    public void createOrUpdateUserSetting(UserSetting userSetting) {
        this.usersSettings.put(userSetting.getPlayerId(), userSetting);
    }

    @Override
    public Optional<UserSetting> findUserSettingByPlayerId(UUID playerId) {
        return Optional.ofNullable(this.usersSettings.get(playerId));
    }

}
