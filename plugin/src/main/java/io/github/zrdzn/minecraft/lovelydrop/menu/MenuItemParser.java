package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import io.github.zrdzn.minecraft.lovelydrop.ParserHelper;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItemCache;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;

public class MenuItemParser {

    private final Logger logger;
    private final DropItemCache dropItemCache;

    public MenuItemParser(Logger logger, DropItemCache dropItemCache) {
        this.logger = logger;
        this.dropItemCache = dropItemCache;
    }

    public MenuItem parse(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        String typeRaw = section.getString("type");
        if (typeRaw == null) {
            throw new InvalidConfigurationException("Key 'type' is null.");
        }

        MaterialData type = ParserHelper.parseLegacyMaterial(typeRaw);

        String itemName = LovelyDropPlugin.color(section.getString("meta.displayname", "<EMPTY>"));

        List<String> itemLore = LovelyDropPlugin.color(section.getStringList("meta.lore"));

        boolean showEnchantments = section.getBoolean("meta.show-enchantments", true);

        ConfigurationSection actionSection = section.getConfigurationSection("click-action");
        if (actionSection == null) {
            throw new InvalidConfigurationException("Section 'click-action' does not exist.");
        }

        Map<ClickType, MenuAction> actions = new HashMap<>();

        // Parsing action section.
        for (String clickType : actionSection.getKeys(false)) {
            String actionRaw = actionSection.getString(clickType);

            MenuAction action;
            try {
                action = MenuAction.valueOf(actionRaw);
            } catch (IllegalArgumentException exception) {
                action = MenuAction.NONE;
            }

            try {
                actions.put(ClickType.valueOf(clickType), action);
            } catch (IllegalArgumentException exception) {
                actions.put(ClickType.UNKNOWN, action);
            }
        }

        if (actions.size() == 0) {
            throw new InvalidConfigurationException("Action map is empty, check your configuration in action section.");
        }

        int slotRow = section.getInt("slot.row");
        int slotColumn = section.getInt("slot.column");

        if (slotRow < 1 || slotColumn < 1) {
            throw new InvalidConfigurationException("Key 'slot.row' and/or 'slot.column' cannot be lower than 1.");
        }

        Entry<Integer, Integer> slot = new AbstractMap.SimpleEntry<>(slotRow, slotColumn);

        return new MenuItem(type, itemName, itemLore, showEnchantments, actions, slot,
            this.dropItemCache.getDrop(section.getName()).orElse(null));
    }

    public List<MenuItem> parseMany(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("items")) {
            throw new InvalidConfigurationException("Provided section is not 'menu.items'.");
        }

        List<MenuItem> items = new ArrayList<>();

        // Add each item section to items list.
        section.getKeys(false).forEach(key -> {
            try {
                items.add(this.parse(section.getConfigurationSection(key)));
            } catch (InvalidConfigurationException exception) {
                this.logger.severe("Something went wrong while parsing menu item section.");
                exception.printStackTrace();
            }
        });

        return items;
    }

}
