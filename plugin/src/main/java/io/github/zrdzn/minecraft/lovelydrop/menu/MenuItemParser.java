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
