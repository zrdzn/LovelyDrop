/*
 * Copyright (c) 2022 zrdzn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.zrdzn.minecraft.lovelydrop.user;

import io.github.zrdzn.minecraft.lovelydrop.drop.DropItemCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

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
