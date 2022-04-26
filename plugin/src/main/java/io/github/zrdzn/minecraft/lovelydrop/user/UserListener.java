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

import io.github.zrdzn.minecraft.lovelydrop.item.ItemCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

    private final UserCache userCache;
    private final ItemCache itemCache;

    public UserListener(UserCache userCache, ItemCache itemCache) {
        this.userCache = userCache;
        this.itemCache = itemCache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        User user = new User(playerId);
        this.itemCache.getDrops().values().forEach(items -> items.forEach(item -> user.addInventoryDrop(item, false)));

        this.userCache.addUser(playerId, user);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.userCache.removeUser(event.getPlayer().getUniqueId());
    }

}
