package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UserSettingFacade {

    private final UserSettingCache userSettingCache;
    private final UserSettingRepository userSettingRepository;

    public UserSettingFacade(UserSettingCache userSettingCache, UserSettingRepository userSettingRepository) {
        this.userSettingCache = userSettingCache;
        this.userSettingRepository = userSettingRepository;
    }

    /**
     * Save all user settings from cache to storage.
     */
    public void saveOrUpdateAllUserSettingsToStorage() {
        this.userSettingRepository.createOrUpdateUserSettings(new ArrayList<>(this.userSettingCache.findAllUserSettings()));
    }

    /**
     * Add user settings to cache.
     *
     * @param playerId a player id
     * @param disabledDrops a set of disabled drops
     * @param dropsToInventory a map of drops to inventory
     */
    public void addUserSettingToCache(UUID playerId, Set<String> disabledDrops, Map<String, Boolean> dropsToInventory) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        if (disabledDrops == null) {
            throw new UserSettingException("Disabled drops cannot be null.");
        }

        if (dropsToInventory == null) {
            throw new UserSettingException("Drops to inventory cannot be null.");
        }

        // Add user settings to cache.
        this.userSettingCache.addUserSetting(new UserSetting(playerId, disabledDrops, dropsToInventory));
    }

    /**
     * Save user settings to storage and remove them from cache.
     *
     * @param playerId a player id
     * @param disabledDrops a set of disabled drops
     * @param dropsToInventory a map of drops to inventory
     */
    public void saveOrUpdateUserSettingToStorage(UUID playerId, Set<String> disabledDrops, Map<String, Boolean> dropsToInventory) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        if (disabledDrops == null) {
            throw new UserSettingException("Disabled drops cannot be null.");
        }

        if (dropsToInventory == null) {
            throw new UserSettingException("Drops to inventory cannot be null.");
        }

        // Save user settings to storage.
        this.userSettingRepository.createOrUpdateUserSetting(new UserSetting(playerId, disabledDrops, dropsToInventory));

        // Remove user settings from cache.
        this.removeUserSettingByPlayerIdFromCache(playerId);
    }

    /**
     * Find user settings by player id from cache.
     *
     * @param playerId a player id
     * @return an optional of user settings
     */
    public Optional<UserSetting> findUserSettingByPlayerIdFromCache(UUID playerId) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        return this.userSettingCache.findUserSettingByPlayerId(playerId);
    }

    /**
     * Find user settings by player id from storage.
     *
     * @param playerId a player id
     * @return an optional of user settings
     */
    public Optional<UserSetting> findUserSettingByPlayerId(UUID playerId) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        // Find user settings from storage.
        return this.userSettingRepository.findUserSettingByPlayerId(playerId);
    }

    /**
     * Remove user settings by player id from cache.
     *
     * @param playerId a player id
     */
    public void removeUserSettingByPlayerIdFromCache(UUID playerId) {
        if (playerId == null) {
            throw new UserSettingException("Player id cannot be null.");
        }

        // Remove user settings from cache.
        this.userSettingCache.removeUserSettingByPlayerId(playerId);
    }

}
