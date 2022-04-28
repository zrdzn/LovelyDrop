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
package io.github.zrdzn.minecraft.lovelydrop.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.zrdzn.minecraft.lovelydrop.item.Item;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import io.github.zrdzn.minecraft.lovelydrop.user.User;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MenuService {

    private final Logger logger;
    private final MessageService messageService;
    private final Menu menu;
    private final UserCache userCache;

    public MenuService(Logger logger, MessageService messageService, Menu menu, UserCache userCache) {
        this.logger = logger;
        this.messageService = messageService;
        this.menu = menu;
        this.userCache = userCache;
    }

    public boolean open(Player player) {
        // Try getting user from the cache.
        Optional<User> userMaybe = this.userCache.getUser(player.getUniqueId());
        if (!userMaybe.isPresent()) {
            this.logger.severe("User " + player.getName() + " is not added to cached users.");
            return false;
        }

        User user = userMaybe.get();

        Gui menu = new Gui(this.menu.getRows(), this.menu.getTitle(), InteractionModifier.VALUES);

        // Forbid the player from getting the item from the inventory.
        menu.setDefaultClickAction(event -> event.setCancelled(true));

        this.menu.getItems().forEach(item -> {
            Entry<Integer, Integer> slot = item.getSlot();
            int row = slot.getKey();
            int column = slot.getValue();

            // Action map cannot be empty because of the exception thrown in the parsing method.
            Map<ClickType, MenuAction> actions = item.getAction();

            // Set item that does not have drop item assigned to it.
            Item dropItem = item.getDropItem();
            if (dropItem == null) {
                menu.setItem(row, column, ItemBuilder.from(item.getType())
                    .setName(item.getDisplayName())
                    .setLore(item.getLore())
                    .asGuiItem(event -> {
                        if (!actions.containsKey(ClickType.UNKNOWN)) {
                            ClickType click = event.getClick();

                            if (actions.containsKey(click) && actions.get(click) == MenuAction.CLOSE_MENU) {
                                menu.close(player, true);
                            }

                            return;
                        }

                        if (actions.get(ClickType.UNKNOWN) == MenuAction.CLOSE_MENU) {
                            menu.close(player, true);
                        }
                    }));

                return;
            }

            Entry<Integer, Integer> amount = dropItem.getAmount();
            String minimumAmount = String.valueOf(amount.getKey());
            String maximumAmount = String.valueOf(amount.getValue());

            Entry<String, String> amountFormat = this.menu.getAmountFormat();

            // Set lore depending on the amount setting.
            List<String> lore = item.getLore().stream()
                .map(line -> {
                    String finalAmount;
                    if (minimumAmount.equals(maximumAmount)) {
                        finalAmount = amountFormat.getKey()
                            .replace("{AMOUNT}", minimumAmount);
                    } else {
                        finalAmount = amountFormat.getValue()
                            .replace("{AMOUNT-MIN}", minimumAmount)
                            .replace("{AMOUNT-MAX}", String.valueOf(maximumAmount));
                    }

                    return line
                        .replace("{CHANCE}", String.valueOf(dropItem.getChance()))
                        .replace("{AMOUNT}", finalAmount)
                        .replace("{EXPERIENCE}", String.valueOf(dropItem.getExperience()));
                })
                .collect(Collectors.toList());

            Entry<String, String> dropSwitch = this.menu.getDropSwitch();
            Entry<String, String> inventoryDropSwitch = this.menu.getInventoryDropSwitch();

            String dropName = dropItem.getDisplayName();

            ItemBuilder menuItemBuilder = ItemBuilder.from(item.getType())
                .setName(item.getDisplayName())
                .setLore(lore.stream()
                    .map(line -> line
                        .replace("{SWITCH}", !user.hasDisabledDrop(dropItem) ? dropSwitch.getKey() : dropSwitch.getValue())
                        .replace("{SWITCH_INVENTORY}", user.hasSwitchedInventoryDrop(dropItem.getId()) ?
                            inventoryDropSwitch.getKey() :
                            inventoryDropSwitch.getValue()))
                    .collect(Collectors.toList()));

            // Add enchantments if they should be shown.
            if (item.isShowEnchantments()) {
                dropItem.getEnchantments().forEach((enchantment, level) -> menuItemBuilder.enchant(enchantment, level, true));
            }

            GuiItem menuItem = menuItemBuilder.asGuiItem();

            // Perform specific actions when player clicks the item.
            menuItem.setAction(event -> {
                String itemId = dropItem.getId();

                if (!actions.containsKey(ClickType.UNKNOWN)) {
                    ClickType click = event.getClick();

                    if (actions.containsKey(click)) {
                        MenuAction action = actions.get(click);
                        if (action == MenuAction.CLOSE_MENU) {
                            menu.close(player, true);
                            return;
                        } else if (action == MenuAction.SWITCH_DROP) {
                            if (user.hasDisabledDrop(dropItem)) {
                                user.enableDrop(dropItem);
                            } else {
                                user.disableDrop(dropItem);
                            }

                            this.messageService.send(player, "drop-switched", "{DROP}", dropName);
                        } else if (action == MenuAction.SWITCH_DROP_TO_INVENTORY) {
                            user.switchInventoryDrop(itemId, !user.hasSwitchedInventoryDrop(itemId));
                            this.messageService.send(player, "drop-switched-inventory", "{DROP}", dropName);
                        }
                    }
                } else {
                    MenuAction action = actions.get(ClickType.UNKNOWN);
                    if (action == MenuAction.CLOSE_MENU) {
                        menu.close(player, true);
                        return;
                    } else if (action == MenuAction.SWITCH_DROP) {
                        if (user.hasDisabledDrop(dropItem)) {
                            user.enableDrop(dropItem);
                        } else {
                            user.disableDrop(dropItem);
                        }

                        this.messageService.send(player, "drop-switched", "{DROP}", dropName);
                    } else if (action == MenuAction.SWITCH_DROP_TO_INVENTORY) {
                        user.switchInventoryDrop(itemId, !user.hasSwitchedInventoryDrop(itemId));
                        this.messageService.send(player, "drop-switched-inventory", "{DROP}", dropName);
                    }
                }

                ItemStack actionItem = ItemBuilder.from(menuItem.getItemStack())
                    .setLore(lore.stream()
                        .map(line -> line
                            .replace("{SWITCH}", !user.hasDisabledDrop(dropItem) ? dropSwitch.getKey() : dropSwitch.getValue())
                            .replace("{SWITCH_INVENTORY}", user.hasSwitchedInventoryDrop(dropItem.getId()) ?
                                inventoryDropSwitch.getKey() :
                                inventoryDropSwitch.getValue()))
                        .collect(Collectors.toList()))
                    .build();

                menu.updateItem(row, column, actionItem);
            });

            menu.setItem(row, column, menuItem);
        });

        // Fill the rest inventory with the specified item if enabled.
        ItemStack filler = this.menu.getFiller();
        if (filler != null) {
            menu.getFiller().fill(ItemBuilder.from(filler).asGuiItem());
        }

        menu.open(player);

        return true;
    }

}
