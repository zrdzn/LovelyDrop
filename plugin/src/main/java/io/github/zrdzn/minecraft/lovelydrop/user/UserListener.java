package io.github.zrdzn.minecraft.lovelydrop.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.UUID;

public class UserListener implements Listener {

    private final UserCache userCache;

    public UserListener(UserCache userCache) {
        this.userCache = userCache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        this.userCache.addUser(playerId, new User(playerId, new HashSet<>()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.userCache.removeUser(event.getPlayer().getUniqueId());
    }

}
