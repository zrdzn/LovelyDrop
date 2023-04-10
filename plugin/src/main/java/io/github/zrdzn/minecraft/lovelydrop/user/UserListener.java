package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.UUID;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItemCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final UserCache userCache;
    private final DropItemCache dropItemCache;
    private final boolean defaultDropToInventory;

    public UserListener(UserCache userCache, DropItemCache dropItemCache, boolean defaultDropToInventory) {
        this.userCache = userCache;
        this.dropItemCache = dropItemCache;
        this.defaultDropToInventory = defaultDropToInventory;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        User user = new User(playerId);
        this.dropItemCache.getDrops().values().forEach(items ->
            items.forEach(item -> user.addInventoryDrop(item, this.defaultDropToInventory)));

        this.userCache.addUser(playerId, user);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.userCache.removeUser(event.getPlayer().getUniqueId());
    }

}
