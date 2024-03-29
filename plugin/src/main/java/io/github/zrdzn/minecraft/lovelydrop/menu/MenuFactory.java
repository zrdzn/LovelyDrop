package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.InteractionModifier;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import eu.okaeri.commons.range.IntRange;
import io.github.zrdzn.minecraft.lovelydrop.PluginConfig;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropConfig;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuConfig.SwitchConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import io.github.zrdzn.minecraft.lovelydrop.shared.IntRangeFormatConfig;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSetting;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MenuFactory {

    private final PluginConfig config;
    private final MessageFacade messageFacade;

    public MenuFactory(PluginConfig config, MessageFacade messageFacade) {
        this.config = config;
        this.messageFacade = messageFacade;
    }

    public Gui createDropMenu(Player player, UserSetting userSetting) {
        MenuConfig menuConfig = this.config.getMenu();

        Gui menu = new Gui(menuConfig.getRows(), menuConfig.getTitle().getText(), InteractionModifier.VALUES);

        // Forbid the player from getting the item from the inventory.
        menu.setDefaultClickAction(event -> event.setCancelled(true));

        menuConfig.getItems().forEach((menuSlotItemKey, menuSlotItem) -> {
            MenuItemConfig.SlotConfig slot = menuSlotItem.getSlot();
            int row = slot.getRow();
            int column = slot.getColumn();

            Map<ClickType, MenuAction> actions = menuSlotItem.getClickAction();

            DropConfig drop = this.config.getDrops().get(menuSlotItemKey);

            // Set item that does not have drop item assigned to it.
            if (drop == null) {
                menu.setItem(row, column, ItemBuilder.from(menuSlotItem.getItem())
                        .asGuiItem(event -> {
                            ClickType click = event.getClick();

                            if (actions.containsKey(click) && actions.get(click) == MenuAction.CLOSE_MENU) {
                                menu.close(player, true);
                            }
                        }));

                return;
            }

            IntRangeFormatConfig heightFormat = menuConfig.getHeightFormat();
            IntRange heightRange = drop.getHeight();
            String minimumHeight = String.valueOf(heightRange.getMin());
            String maximumHeight = String.valueOf(heightRange.getMax());

            ItemStack menuItemStack = menuSlotItem.getItem();
            List<String> lore = menuItemStack.getItemMeta().getLore().stream()
                    .map(line -> {
                        String formattedHeight;
                        if (minimumHeight.equals(maximumHeight)) {
                            formattedHeight = StringUtils.replace(heightFormat.getFixed().getText(), "{HEIGHT}", minimumHeight);
                        } else {
                            formattedHeight = StringUtils.replaceEach(heightFormat.getDifferent().getText(),
                                    new String[]{ "{HEIGHT-MIN}", "{HEIGHT-MAX}" },
                                    new String[]{ minimumHeight, maximumHeight });
                        }

                        return StringUtils.replace(line, "{HEIGHT}", formattedHeight);
                    })
                    .collect(Collectors.toList());

            Map<Integer, DropConfig.FortuneConfig> fortunes = drop.getFortune();
            for (int level = 0; level < fortunes.size(); level++) {
                DropConfig.FortuneConfig fortune = fortunes.get(level);

                IntRangeFormatConfig amountFormat = menuConfig.getAmountFormat();
                IntRange amountRange = fortune.getAmount();
                String minimumAmount = String.valueOf(amountRange.getMin());
                String maximumAmount = String.valueOf(amountRange.getMax());

                for (int lineNumber = 0; lineNumber < lore.size(); lineNumber++) {
                    // Format lore with the chance and experience.
                    String line = StringUtils.replaceEach(lore.get(lineNumber),
                            new String[]{ String.format("{CHANCE-%d}", level), String.format("{EXPERIENCE-%d}", level) },
                            new String[]{ fortune.getChance().getFormattedValue(), String.valueOf(fortune.getExperience()) });

                    // Format lore with the fixed amount.
                    String placeholder = String.format("{AMOUNT-%d}", level);
                    if (minimumAmount.equals(maximumAmount)) {
                        line = StringUtils.replace(line, placeholder,
                                StringUtils.replace(amountFormat.getFixed().getText(), "{AMOUNT}", minimumAmount));
                    } else {
                        line = StringUtils.replace(line, placeholder,
                                StringUtils.replaceEach(amountFormat.getDifferent().getText(),
                                        new String[]{ "{AMOUNT-MIN}", "{AMOUNT-MAX}" },
                                        new String[]{ minimumAmount, maximumAmount }));
                    }

                    lore.set(lineNumber, line);
                }
            }

            MenuConfig.SwitchConfig dropSwitch = menuConfig.getDropSwitch();
            MenuConfig.SwitchConfig inventorySwitch = menuConfig.getInventoryDropSwitch();

            ItemBuilder menuItemBuilder = ItemBuilder.from(menuItemStack)
                    .setLore(this.formatLore(lore, userSetting, menuSlotItemKey, dropSwitch, inventorySwitch));

            GuiItem menuItem = menuItemBuilder.asGuiItem();

            // Perform specific actions when player clicks the item.
            menuItem.setAction(event -> {
                ClickType click = event.getClick();
                if (actions.containsKey(click)) {
                    MenuAction action = actions.get(click);
                    this.handleClickAction(player, userSetting, menu, menuSlotItemKey, action);
                }

                ItemStack actionItem = ItemBuilder.from(menuItem.getItemStack())
                        .setLore(this.formatLore(lore, userSetting, menuSlotItemKey, dropSwitch, inventorySwitch))
                        .build();

                menu.updateItem(row, column, actionItem);
            });

            menu.setItem(row, column, menuItem);
        });

        // Fill the rest inventory with the specified item if enabled.
        MenuConfig.FillerConfig filler = menuConfig.getFiller();
        if (filler.isEnabled()) {
            menu.getFiller().fill(ItemBuilder.from(filler.getItem()).asGuiItem());
        }

        return menu;
    }

    private void handleClickAction(Player player, UserSetting userSetting, Gui menu, String menuSlotItemKey, MenuAction action) {
        if (action == MenuAction.CLOSE_MENU) {
            menu.close(player, true);
        } else if (action == MenuAction.SWITCH_DROP) {
            if (userSetting.hasDisabledDrop(menuSlotItemKey)) {
                userSetting.removeDisabledDrop(menuSlotItemKey);
            } else {
                userSetting.addDisabledDrop(menuSlotItemKey);
            }

            this.messageFacade.sendMessage(player, this.config.getMessages().getDropSwitched(), "{DROP}", menuSlotItemKey);
        } else if (action == MenuAction.SWITCH_DROP_TO_INVENTORY) {
            userSetting.setDropToInventory(menuSlotItemKey, !userSetting.hasDropToInventory(menuSlotItemKey));
            this.messageFacade.sendMessage(player, this.config.getMessages().getDropSwitchedInventory(), "{DROP}", menuSlotItemKey);
        }
    }

    private List<String> formatLore(List<String> lore, UserSetting userSetting, String menuSlotItemKey, SwitchConfig dropSwitch,
                                    SwitchConfig inventorySwitch) {
        return lore.stream()
                .map(line -> {
                    String[] replacements = {
                            userSetting.hasDisabledDrop(menuSlotItemKey) ? dropSwitch.getDisabled().getText() : dropSwitch.getEnabled().getText(),
                            userSetting.hasDropToInventory(menuSlotItemKey) ? inventorySwitch.getEnabled().getText() : inventorySwitch.getDisabled().getText()
                    };

                    return StringUtils.replaceEach(line, new String[]{ "{SWITCH}", "{SWITCH_INVENTORY}" }, replacements);
                })
                .collect(Collectors.toList());
    }

}
