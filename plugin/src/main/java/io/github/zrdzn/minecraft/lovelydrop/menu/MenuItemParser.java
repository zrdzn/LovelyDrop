package io.github.zrdzn.minecraft.lovelydrop.menu;

import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import io.github.zrdzn.minecraft.lovelydrop.item.ItemCache;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class MenuItemParser {

    private final Logger logger;
    private final ItemCache itemCache;

    public MenuItemParser(Logger logger, ItemCache itemCache) {
        this.logger = logger;
        this.itemCache = itemCache;
    }

    public MenuItem parse(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        String typeRaw = section.getString("type");
        if (typeRaw == null) {
            throw new InvalidConfigurationException("Key 'type' is null.");
        }

        Material type = Material.matchMaterial(typeRaw);
        if (type == null) {
            throw new InvalidConfigurationException("Material with key 'type' does not exist.");
        }

        String itemName = LovelyDropPlugin.color(section.getString("meta.displayname", "<EMPTY>"));

        List<String> itemLore = LovelyDropPlugin.color(section.getStringList("meta.lore"));

        MenuAction action = MenuAction.valueOf(section.getString("action"));

        int slotRow = section.getInt("slot.row");
        int slotColumn = section.getInt("slot.column");

        if (slotRow < 1 || slotColumn < 1) {
            throw new InvalidConfigurationException("Key 'slot.row' and/or 'slot.column' cannot be lower than 1.");
        }

        Entry<Integer, Integer> slot = new AbstractMap.SimpleEntry<>(slotRow, slotColumn);

        return new MenuItem(type, itemName, itemLore, action, slot, this.itemCache.getDrop(section.getName()).orElse(null));
    }

    public List<MenuItem> parseMany(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("items")) {
            throw new InvalidConfigurationException("Provided section is not 'menu.items'.");
        }

        List<MenuItem> items = new ArrayList<>();

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
