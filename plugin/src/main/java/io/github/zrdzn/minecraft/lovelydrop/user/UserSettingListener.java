package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for handling user settings.
 */
public class UserSettingListener implements Listener {

    private final Logger logger = LoggerFactory.getLogger(UserSettingListener.class);

    private final Plugin plugin;
    private final BukkitScheduler scheduler;
    private final UserSettingFacade userSettingFacade;
    private final Map<String, Boolean> defaultDropsToInventory;

    public UserSettingListener(Plugin plugin, UserSettingFacade userSettingFacade, Map<String, Boolean> defaultDropsToInventory) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.userSettingFacade = userSettingFacade;
        this.defaultDropsToInventory = defaultDropsToInventory;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String name = player.getName();

        // Remove any old cache.
        if (this.userSettingFacade.findUserSettingByPlayerIdFromCache(playerId).isPresent()) {
            this.userSettingFacade.removeUserSettingByPlayerIdFromCache(playerId);
        }

        this.scheduler.runTaskAsynchronously(this.plugin, () -> {
            try {
                // Try to find user settings in storage, if found, add to cache.
                Optional<UserSetting> userSettingMaybe = this.userSettingFacade.findUserSettingByPlayerId(playerId);
                if (userSettingMaybe.isPresent()) {
                    UserSetting userSetting = userSettingMaybe.get();
                    this.userSettingFacade.addUserSettingToCache(userSetting.getPlayerId(), userSetting.getDisabledDrops(), userSetting.getDropsToInventory());
                    this.logger.info("Drop settings for {} were found and successfully loaded.", name);
                    return;
                }

                this.logger.info("Drop settings for {} not found, creating new ones.", name);
                this.userSettingFacade.addUserSettingToCache(playerId, new HashSet<>(), this.defaultDropsToInventory);
            } catch (UserSettingException exception) {
                this.logger.error("Could not find or create user setting.", exception);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();

        Optional<UserSetting> userSettingMaybe = this.userSettingFacade.findUserSettingByPlayerIdFromCache(event.getPlayer().getUniqueId());
        if (!userSettingMaybe.isPresent()) {
            this.logger.warn("Drop settings for {} may have been reset due to their absence in the cache.", name);
            return;
        }

        UserSetting userSetting = userSettingMaybe.get();
        this.scheduler.runTaskAsynchronously(this.plugin, () -> {
            try {
                this.userSettingFacade.saveOrUpdateUserSettingToStorage(userSetting.getPlayerId(), userSetting.getDisabledDrops(), userSetting.getDropsToInventory());
                this.logger.info("Drop settings for {} were successfully saved.", name);
            } catch (UserSettingException exception) {
                this.logger.error("Could not save or update user settings.", exception);
            }
        });
    }

}
