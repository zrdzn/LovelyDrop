package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSettingRepository {

    void createOrUpdateUserSettings(List<UserSetting> userSettings);

    void createOrUpdateUserSetting(UserSetting userSetting);

    Optional<UserSetting> findUserSettingByPlayerId(UUID playerId);
}
