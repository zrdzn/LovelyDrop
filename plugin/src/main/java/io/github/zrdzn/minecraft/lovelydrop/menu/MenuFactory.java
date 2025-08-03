package io.github.zrdzn.minecraft.lovelydrop.menu;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuFactory {

    private final PluginConfig config;
    private final MessageFacade messageFacade;

    public MenuFactory(PluginConfig config, MessageFacade messageFacade) {
        this.config = config;
        this.messageFacade = messageFacade;
    }

    public Gui createDropMenu(Player player, UserSetting userSetting) {
        MenuConfig menuConfig = this.config.menu;

        Gui menu = new Gui(menuConfig.rows, menuConfig.title.getText(), InteractionModifier.VALUES);

        // Forbid the player from getting the item from the inventory.
        menu.setDefaultClickAction(event -> event.setCancelled(true));

        setupMenuItems(menu, player, userSetting, menuConfig, menuConfig.items);

        // Fill the rest inventory with the specified item if enabled.
        MenuConfig.FillerConfig filler = menuConfig.filler;
        if (filler.enabled) {
            menu.getFiller().fill(ItemBuilder.from(filler.item).asGuiItem());
        }

        return menu;
    }

    private void setupMenuItems(Gui menu, Player player, UserSetting userSetting,
            MenuConfig menuConfig, Map<String, MenuItemConfig> menuConfigItems) {
        menuConfigItems.forEach((menuSlotItemKey, menuSlotItem) -> {
            MenuItemConfig.SlotConfig slot = menuSlotItem.slot;
            int row = slot.row;
            int column = slot.column;

            DropConfig drop = this.config.drops.get(menuSlotItemKey);
            if (drop == null) {
                setDefaultItem(menu, row, column, menuSlotItem.item, menuSlotItem.clickAction,
                        player);
                return;
            }

            List<String> lore = formatLoreForDrop(menuSlotItem.item, drop, menuConfig.heightFormat);
            setItemWithDrop(menu, row, column, menuSlotItem.item, menuSlotItem.clickAction, player,
                    userSetting, menuSlotItemKey, drop, menuConfig, lore);
        });
    }

    private void setDefaultItem(Gui menu, int row, int column, ItemStack item,
            Map<ClickType, MenuAction> actions, Player player) {
        menu.setItem(row, column, ItemBuilder.from(item).asGuiItem(event -> {
            ClickType click = event.getClick();
            if (actions.containsKey(click) && actions.get(click) == MenuAction.CLOSE_MENU) {
                menu.close(player, true);
            }
        }));
    }

    private List<String> formatLoreForDrop(ItemStack itemStack, DropConfig drop,
            IntRangeFormatConfig heightFormat) {
        IntRange heightRange = drop.height;
        String minimumHeight = String.valueOf(heightRange.getMin());
        String maximumHeight = String.valueOf(heightRange.getMax());

        return itemStack.getItemMeta().getLore().stream().map(line -> {
            String formattedHeight = formatHeight(minimumHeight, maximumHeight, heightFormat);
            return StringUtils.replace(line, "{HEIGHT}", formattedHeight);
        }).collect(Collectors.toList());
    }

    private String formatHeight(String minHeight, String maxHeight,
            IntRangeFormatConfig heightFormat) {
        if (minHeight.equals(maxHeight)) {
            return StringUtils.replace(heightFormat.getFixed().getText(), "{HEIGHT}", minHeight);
        } else {
            return StringUtils.replaceEach(heightFormat.getDifferent().getText(),
                    new String[] {"{HEIGHT-MIN}", "{HEIGHT-MAX}"},
                    new String[] {minHeight, maxHeight});
        }
    }

    private void setItemWithDrop(Gui menu, int row, int column, ItemStack item,
            Map<ClickType, MenuAction> actions, Player player, UserSetting userSetting,
            String menuSlotItemKey, DropConfig drop, MenuConfig menuConfig, List<String> lore) {
        for (int level = 0; level < drop.fortune.size(); level++) {
            formatLoreForFortune(lore, level, drop.fortune.get(level), menuConfig.amountFormat);
        }

        MenuConfig.SwitchConfig dropSwitch = menuConfig.dropSwitch;
        MenuConfig.SwitchConfig inventorySwitch = menuConfig.inventoryDropSwitch;

        ItemBuilder menuItemBuilder = ItemBuilder.from(item).setLore(
                this.formatLore(lore, userSetting, menuSlotItemKey, dropSwitch, inventorySwitch));

        GuiItem menuItem = menuItemBuilder.asGuiItem();
        menuItem.setAction(event -> handleItemClick(event, actions, player, userSetting, menu,
                menuSlotItemKey, dropSwitch, inventorySwitch, lore));
        menu.setItem(row, column, menuItem);
    }

    private void formatLoreForFortune(List<String> lore, int level,
            DropConfig.FortuneConfig fortune, IntRangeFormatConfig amountFormat) {
        IntRange amountRange = fortune.amount;
        String minimumAmount = String.valueOf(amountRange.getMin());
        String maximumAmount = String.valueOf(amountRange.getMax());

        lore.replaceAll(line -> formatLineWithFortune(line, level, fortune, amountFormat,
                minimumAmount, maximumAmount));
    }

    private String formatLineWithFortune(String line, int level, DropConfig.FortuneConfig fortune,
            IntRangeFormatConfig amountFormat, String minAmount, String maxAmount) {
        line = StringUtils.replaceEach(line,
                new String[] {String.format("{CHANCE-%d}", level),
                                String.format("{EXPERIENCE-%d}", level)},
                new String[] {fortune.chance.getFormattedValue(),
                                String.valueOf(fortune.experience)});

        String placeholder = String.format("{AMOUNT-%d}", level);
        if (minAmount.equals(maxAmount)) {
            return StringUtils.replace(line, placeholder,
                    StringUtils.replace(amountFormat.getFixed().getText(), "{AMOUNT}", minAmount));
        } else {
            return StringUtils.replace(line, placeholder,
                    StringUtils.replaceEach(amountFormat.getDifferent().getText(),
                            new String[] {"{AMOUNT-MIN}", "{AMOUNT-MAX}"},
                            new String[] {minAmount, maxAmount}));
        }
    }

    private void handleItemClick(InventoryClickEvent event, Map<ClickType, MenuAction> actions,
            Player player, UserSetting userSetting, Gui menu, String menuSlotItemKey,
            MenuConfig.SwitchConfig dropSwitch, MenuConfig.SwitchConfig inventorySwitch,
            List<String> lore) {
        ClickType click = event.getClick();
        if (actions.containsKey(click)) {
            MenuAction action = actions.get(click);
            this.handleClickAction(player, userSetting, menu, menuSlotItemKey, action);
        }

        ItemStack actionItem = ItemBuilder.from(event.getCurrentItem()).setLore(
                this.formatLore(lore, userSetting, menuSlotItemKey, dropSwitch, inventorySwitch))
                .build();

        menu.updateItem(event.getSlot(), actionItem);
    }

    private void handleClickAction(Player player, UserSetting userSetting, Gui menu,
            String menuSlotItemKey, MenuAction action) {
        switch (action) {
            case CLOSE_MENU:
                closeMenu(player, menu);
                break;
            case SWITCH_DROP:
                toggleDrop(player, userSetting, menuSlotItemKey);
                break;
            case SWITCH_DROP_TO_INVENTORY:
                toggleDropToInventory(player, userSetting, menuSlotItemKey);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    private void closeMenu(Player player, Gui menu) {
        menu.close(player, true);
    }

    private void toggleDrop(Player player, UserSetting userSetting, String menuSlotItemKey) {
        if (userSetting.hasDisabledDrop(menuSlotItemKey)) {
            userSetting.removeDisabledDrop(menuSlotItemKey);
        } else {
            userSetting.addDisabledDrop(menuSlotItemKey);
        }
        this.messageFacade.sendMessage(player, this.config.messages.dropSwitched, "{DROP}",
                menuSlotItemKey);
    }

    private void toggleDropToInventory(Player player, UserSetting userSetting,
            String menuSlotItemKey) {
        userSetting.setDropToInventory(menuSlotItemKey,
                !userSetting.hasDropToInventory(menuSlotItemKey));
        this.messageFacade.sendMessage(player, this.config.messages.dropSwitchedInventory, "{DROP}",
                menuSlotItemKey);
    }

    private List<String> formatLore(List<String> lore, UserSetting userSetting,
            String menuSlotItemKey, SwitchConfig dropSwitch, SwitchConfig inventorySwitch) {
        return lore.stream().map(
                line -> formatLine(line, userSetting, menuSlotItemKey, dropSwitch, inventorySwitch))
                .collect(Collectors.toList());
    }

    private String formatLine(String line, UserSetting userSetting, String menuSlotItemKey,
            SwitchConfig dropSwitch, SwitchConfig inventorySwitch) {
        String[] replacements =
                getReplacements(userSetting, menuSlotItemKey, dropSwitch, inventorySwitch);
        return StringUtils.replaceEach(line, new String[] {"{SWITCH}", "{SWITCH_INVENTORY}"},
                replacements);
    }

    private String[] getReplacements(UserSetting userSetting, String menuSlotItemKey,
            SwitchConfig dropSwitch, SwitchConfig inventorySwitch) {
        String dropText =
                userSetting.hasDisabledDrop(menuSlotItemKey) ? dropSwitch.disabled.getText()
                        : dropSwitch.enabled.getText();
        String inventoryText =
                userSetting.hasDropToInventory(menuSlotItemKey) ? inventorySwitch.enabled.getText()
                        : inventorySwitch.disabled.getText();
        return new String[] {dropText, inventoryText};
    }
}
