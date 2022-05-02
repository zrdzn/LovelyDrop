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
package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.item.Item;
import io.github.zrdzn.minecraft.lovelydrop.item.ItemCache;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import io.github.zrdzn.minecraft.lovelydrop.user.User;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import io.github.zrdzn.minecraft.spigot.SpigotAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class DropListener implements Listener {

    private final Logger logger;
    private final MessageService messageService;
    private final SpigotAdapter adapter;
    private final ItemCache itemCache;
    private final UserCache userCache;

    public DropListener(Logger logger, MessageService messageService, SpigotAdapter adapter, ItemCache itemCache,
                        UserCache userCache) {
        this.logger = logger;
        this.messageService = messageService;
        this.adapter = adapter;
        this.itemCache = itemCache;
        this.userCache = userCache;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSourceBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();

        MaterialData source = block.getState().getData();

        // Get optional drops from source blocks.
        Set<Item> sourceDrops = this.itemCache.getDrops(source);
        if (sourceDrops.isEmpty()) {
            return;
        }

        // Get user from the cache.
        Optional<User> userMaybe = this.userCache.getUser(player.getUniqueId());
        if (!userMaybe.isPresent()) {
            this.logger.severe("User " + player.getName() + " is not added to cached users.");
            return;
        }

        this.adapter.getBlockBreakHelper().disableDrop(event);

        User user = userMaybe.get();

        // Remove all disabled drops from the source drops list.
        sourceDrops.removeIf(sourceDrop -> user.getDisabledDrops().contains(sourceDrop));

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int blockHeight = block.getY();

        sourceDrops.forEach(item -> {
            Entry<Integer, Integer> height = item.getHeight();
            if (blockHeight < height.getKey() || blockHeight > height.getValue()) {
                return;
            }

            if (item.getChance() <= Math.random() * 100.0D) {
                return;
            }

            player.giveExp(item.getExperience());

            Entry<Integer, Integer> amountEntry = item.getAmount();

            int amount = amountEntry.getKey();
            if (amount != amountEntry.getValue()) {
                amount = random.nextInt(item.getAmount().getKey(), item.getAmount().getValue());
            }

            ItemStack droppedItem = item.getType().toItemStack(amount);

            ItemMeta droppedItemMeta = droppedItem.getItemMeta();

            droppedItemMeta.setDisplayName(item.getDisplayName());

            String[] placeholders = { "{DROP}", item.getId(), "{AMOUNT}", String.valueOf(amount) };
            this.messageService.send(player, "drop-successful", placeholders);

            droppedItemMeta.setLore(item.getLore());
            droppedItem.setItemMeta(droppedItemMeta);

            // Add additional enchantments.
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            if (enchantments.size() > 0) {
                droppedItem.addUnsafeEnchantments(enchantments);
            }

            World world = player.getWorld();

            Location location = block.getLocation();

            if (user.hasSwitchedInventoryDrop(item.getId())) {
                Map<Integer, ItemStack> itemsLeft = player.getInventory().addItem(droppedItem);

                // Drop items on the floor if player has full inventory.
                itemsLeft.forEach((key, value) ->
                    world.dropItemNaturally(location, value));

                return;
            }

            world.dropItemNaturally(location, droppedItem);
        });
    }

}
