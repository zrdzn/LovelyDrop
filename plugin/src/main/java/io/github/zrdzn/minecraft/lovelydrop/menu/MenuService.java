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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItem;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropProperty;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import io.github.zrdzn.minecraft.lovelydrop.user.User;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
            DropItem dropItem = item.getDropItem();
            if (dropItem == null) {
                menu.setItem(row, column, ItemBuilder.from(item.getType().toItemStack(1))
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

            Entry<String, String> heightFormat = this.menu.getHeightFormat();
            Entry<Integer, Integer> height = dropItem.getHeight();
            String minimumHeight = String.valueOf(height.getKey());
            String maximumHeight = String.valueOf(height.getValue());

            List<String> lore = item.getLore().stream()
                .map(line -> {
                    String formattedHeight;
                    if (minimumHeight.equals(maximumHeight)) {
                        formattedHeight = StringUtils.replace(heightFormat.getKey(), "{HEIGHT}", minimumHeight);
                    } else {
                        formattedHeight = StringUtils.replaceEach(heightFormat.getValue(),
                            new String[]{ "{HEIGHT-MIN}", "{HEIGHT-MAX}" },
                            new String[]{ minimumHeight, maximumHeight });
                    }

                    return StringUtils.replace(line, "{HEIGHT}", formattedHeight);
                })
                .collect(Collectors.toList());

            Map<Integer, DropProperty> properties = dropItem.getProperties();
            for (int level = 0; level < properties.size(); level++) {
                DropProperty property = properties.get(level);

                Entry<String, String> amountFormat = this.menu.getAmountFormat();
                Entry<Integer, Integer> amount = property.getAmount();
                String minimumAmount = String.valueOf(amount.getKey());
                String maximumAmount = String.valueOf(amount.getValue());

                for (int lineNumber = 0; lineNumber < lore.size(); lineNumber++) {
                    String line = StringUtils.replaceEach(lore.get(lineNumber),
                        new String[]{ String.format("{CHANCE-%d}", level), String.format("{EXPERIENCE-%d}", level) },
                        new String[]{ property.getFormattedChance(), String.valueOf(property.getExperience()) });

                    String placeholder = String.format("{AMOUNT-%d}", level);
                    if (minimumAmount.equals(maximumAmount)) {
                        line = StringUtils.replace(line, placeholder,
                            StringUtils.replace(amountFormat.getKey(), "{AMOUNT}", minimumAmount));
                    } else {
                        line = StringUtils.replace(line, placeholder,
                            StringUtils.replaceEach(amountFormat.getValue(),
                                new String[]{ "{AMOUNT-MIN}", "{AMOUNT-MAX}" },
                                new String[]{ minimumAmount, String.valueOf(maximumAmount) }));
                    }

                    lore.set(lineNumber, line);
                }
            }

            Entry<String, String> dropSwitch = this.menu.getDropSwitch();
            Entry<String, String> inventorySwitch = this.menu.getInventoryDropSwitch();

            String dropId = dropItem.getId();

            ItemBuilder menuItemBuilder = ItemBuilder.from(item.getType().toItemStack(1))
                .setName(item.getDisplayName())
                .setLore(lore.stream()
                    .map(line -> {
                        String[] replacements = {
                            !user.hasDisabledDrop(dropItem) ? dropSwitch.getKey() : dropSwitch.getValue(),
                            user.hasSwitchedInventoryDrop(dropItem.getId()) ? inventorySwitch.getKey() :
                                inventorySwitch.getValue()
                        };

                        return StringUtils
                            .replaceEach(line, new String[]{ "{SWITCH}", "{SWITCH_INVENTORY}" }, replacements);
                    })
                    .collect(Collectors.toList()));

            // Add enchantments if they should be shown.
            if (item.isShowEnchantments()) {
                dropItem.getEnchantments().forEach((enchantment, level) ->
                    menuItemBuilder.enchant(enchantment, level, true));
            }

            GuiItem menuItem = menuItemBuilder.asGuiItem();

            // Perform specific actions when player clicks the item.
            List<String> finalLore = lore;
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

                            this.messageService.send(player, "drop-switched", "{DROP}", dropId);
                        } else if (action == MenuAction.SWITCH_DROP_TO_INVENTORY) {
                            user.switchInventoryDrop(itemId, !user.hasSwitchedInventoryDrop(itemId));
                            this.messageService.send(player, "drop-switched-inventory", "{DROP}", dropId);
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

                        this.messageService.send(player, "drop-switched", "{DROP}", dropId);
                    } else if (action == MenuAction.SWITCH_DROP_TO_INVENTORY) {
                        user.switchInventoryDrop(itemId, !user.hasSwitchedInventoryDrop(itemId));
                        this.messageService.send(player, "drop-switched-inventory", "{DROP}", dropId);
                    }
                }

                ItemStack actionItem = ItemBuilder.from(menuItem.getItemStack())
                    .setLore(finalLore.stream()
                        .map(line -> {
                            String[] replacements = {
                                !user.hasDisabledDrop(dropItem) ? dropSwitch.getKey() : dropSwitch.getValue(),
                                user.hasSwitchedInventoryDrop(dropItem.getId()) ? inventorySwitch.getKey() :
                                    inventorySwitch.getValue()
                            };

                            return StringUtils
                                .replaceEach(line, new String[]{ "{SWITCH}", "{SWITCH_INVENTORY}" }, replacements);
                        })
                        .collect(Collectors.toList()))
                    .build();

                menu.updateItem(row, column, actionItem);
            });

            menu.setItem(row, column, menuItem);
        });

        // Fill the rest inventory with the specified item if enabled.
        this.menu.getFiller().ifPresent(filler -> menu.getFiller().fill(ItemBuilder.from(filler).asGuiItem()));

        menu.open(player);

        return true;
    }

}
