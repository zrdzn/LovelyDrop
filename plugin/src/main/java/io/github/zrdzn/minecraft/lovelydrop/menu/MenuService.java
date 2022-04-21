package io.github.zrdzn.minecraft.lovelydrop.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.zrdzn.minecraft.lovelydrop.item.Item;
import io.github.zrdzn.minecraft.lovelydrop.user.User;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MenuService {

    private final Logger logger;
    private final Menu menu;
    private final UserCache userCache;

    public MenuService(Logger logger, Menu menu, UserCache userCache) {
        this.logger = logger;
        this.menu = menu;
        this.userCache = userCache;
    }

    public boolean open(Player player) {
        Optional<User> userMaybe = this.userCache.getUser(player.getUniqueId());
        if (!userMaybe.isPresent()) {
            this.logger.severe("User " + player.getName() + " is not added to cached users.");
            return false;
        }

        User user = userMaybe.get();

        Gui menu = new Gui(this.menu.getRows(), this.menu.getTitle(), InteractionModifier.VALUES);

        menu.setDefaultClickAction(event -> event.setCancelled(true));

        this.menu.getItems().forEach(item -> {
            Entry<Integer, Integer> slot = item.getSlot();
            int row = slot.getKey();
            int column = slot.getValue();

            Item dropItem = item.getDropItem();
            if (dropItem == null) {
                menu.setItem(row, column, ItemBuilder.from(item.getType())
                    .setName(item.getDisplayName())
                    .setLore(item.getLore())
                    .asGuiItem(event -> {
                        if (item.getAction() == MenuAction.CLOSE_MENU) {
                            menu.close(player, true);
                        }
                    }));

                return;
            }

            Entry<Integer, Integer> amount = dropItem.getAmount();

            Entry<String, String> dropSwitch = this.menu.getDropSwitch();

            GuiItem menuItem = ItemBuilder.from(item.getType())
                .setName(item.getDisplayName())
                .setLore(item.getLore().stream()
                    .map(line -> line.replace("{CHANCE}", String.valueOf(dropItem.getChance()))
                        .replace("{AMOUNT-MIN}", String.valueOf(amount.getKey()))
                        .replace("{AMOUNT-MAX}", String.valueOf(amount.getValue()))
                        .replace("{EXPERIENCE}", String.valueOf(dropItem.getExperience()))
                        .replace("{SWITCH}", !user.hasDisabledDrop(dropItem) ? dropSwitch.getKey() : dropSwitch.getValue()))
                    .collect(Collectors.toList()))
                .asGuiItem();

            menuItem.setAction(event -> {
                if (item.getAction() == MenuAction.CLOSE_MENU) {
                    menu.close(player, true);
                } else if (item.getAction() == MenuAction.SWITCH_DROP) {
                    if (user.hasDisabledDrop(dropItem)) {
                        user.enableDrop(dropItem);
                    } else {
                        user.disableDrop(dropItem);
                    }
                }

                ItemStack actionItem = ItemBuilder.from(menuItem.getItemStack())
                    .setLore(item.getLore().stream()
                        .map(line -> line.replace("{CHANCE}", String.valueOf(dropItem.getChance()))
                            .replace("{AMOUNT-MIN}", String.valueOf(amount.getKey()))
                            .replace("{AMOUNT-MAX}", String.valueOf(amount.getValue()))
                            .replace("{EXPERIENCE}", String.valueOf(dropItem.getExperience()))
                            .replace("{SWITCH}", !user.hasDisabledDrop(dropItem) ? dropSwitch.getKey() : dropSwitch.getValue()))
                        .collect(Collectors.toList()))
                    .build();

                menu.updateItem(row, column, actionItem);
            });

            menu.setItem(row, column, menuItem);
        });


        MenuFiller filler = this.menu.getFiller();
        if (filler.isEnabled()) {
            ItemStack fillerItem = new ItemStack(filler.getType());
            ItemMeta fillerMeta = fillerItem.getItemMeta();
            if (filler.getDisplayName().equalsIgnoreCase("none")) {
                fillerMeta.setDisplayName(" ");
            } else {
                fillerMeta.setDisplayName(filler.getDisplayName());
            }

            fillerItem.setItemMeta(fillerMeta);

            menu.getFiller().fill(ItemBuilder.from(fillerItem).asGuiItem());
        }

        menu.open(player);

        return true;
    }

}
