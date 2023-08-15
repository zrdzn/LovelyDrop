package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User setting repository.
 */
public interface UserSettingRepository {

    /**
     * Create or update multiple users settings.
     *
     * @param userSettings a list of users settings
     */
    void createOrUpdateUserSettings(List<UserSetting> userSettings);

    /**
     * Create or update single user settings.
     *
     * @param userSetting user settings
     */
    void createOrUpdateUserSetting(UserSetting userSetting);

    /**
     * Remove user settings by player id.
     *
     * @param playerId a player id
     *
     * @return the optional user settings
     */
    Optional<UserSetting> findUserSettingByPlayerId(UUID playerId);

}
