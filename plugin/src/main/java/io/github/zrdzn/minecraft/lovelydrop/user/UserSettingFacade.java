package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UserSettingFacade {

    private final UserSettingCache userSettingCache;
    private final UserSettingRepository userSettingRepository;

    public UserSettingFacade(UserSettingCache userSettingCache,
            UserSettingRepository userSettingRepository) {
        this.userSettingCache = userSettingCache;
        this.userSettingRepository = userSettingRepository;
    }

    public void saveOrUpdateAllUserSettingsToStorage() {
        this.userSettingRepository.createOrUpdateUserSettings(
                new ArrayList<>(this.userSettingCache.findAllUserSettings()));
    }

    public void addUserSettingToCache(UUID playerId, Set<String> disabledDrops,
            Map<String, Boolean> dropsToInventory) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        if (disabledDrops == null) {
            throw new UserSettingException("Disabled drops cannot be null.");
        }

        if (dropsToInventory == null) {
            throw new UserSettingException("Drops to inventory cannot be null.");
        }

        this.userSettingCache
                .addUserSetting(new UserSetting(playerId, disabledDrops, dropsToInventory));
    }

    public void saveOrUpdateUserSettingToStorage(UUID playerId, Set<String> disabledDrops,
            Map<String, Boolean> dropsToInventory) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        if (disabledDrops == null) {
            throw new UserSettingException("Disabled drops cannot be null.");
        }

        if (dropsToInventory == null) {
            throw new UserSettingException("Drops to inventory cannot be null.");
        }

        this.userSettingRepository.createOrUpdateUserSetting(
                new UserSetting(playerId, disabledDrops, dropsToInventory));

        this.removeUserSettingByPlayerIdFromCache(playerId);
    }

    public Optional<UserSetting> findUserSettingByPlayerIdFromCache(UUID playerId) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        return this.userSettingCache.findUserSettingByPlayerId(playerId);
    }

    public Optional<UserSetting> findUserSettingByPlayerId(UUID playerId) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        return this.userSettingRepository.findUserSettingByPlayerId(playerId);
    }

    public void removeUserSettingByPlayerIdFromCache(UUID playerId) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        this.userSettingCache.removeUserSettingByPlayerId(playerId);
    }
}
