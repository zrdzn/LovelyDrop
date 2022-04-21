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
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class MenuParser {

    private final Logger logger;
    private final ItemCache itemCache;

    public MenuParser(Logger logger, ItemCache itemCache) {
        this.logger = logger;
        this.itemCache = itemCache;
    }

    public Menu parse(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("menu")) {
            throw new InvalidConfigurationException("Provided section is not 'menu'.");
        }

        String title = LovelyDropPlugin.color(section.getString("title", "Menu"));

        int rows = section.getInt("rows");
        if (rows <= 0) {
            throw new InvalidConfigurationException("Key 'rows' cannot be 0 and lower");
        }

        boolean fillerEnabled = section.getBoolean("filler.enabled", false);

        String fillerTypeRaw = section.getString("filler.type");
        if (fillerTypeRaw == null) {
            throw new InvalidConfigurationException("Key 'filler.type' is null.");
        }

        Material fillerType = Material.matchMaterial(fillerTypeRaw);
        if (fillerType == null) {
            throw new InvalidConfigurationException("Material with key 'filler.type' does not exist.");
        }

        String fillerName = LovelyDropPlugin.color(section.getString("filler.displayname", "none"));

        MenuFiller filler = new MenuFiller(fillerEnabled, fillerType, fillerName);

        String dropSwitchEnabled = LovelyDropPlugin.color(section.getString("drop-switch.enabled", "&aon"));
        String dropSwitchDisabled = LovelyDropPlugin.color(section.getString("drop-switch.disabled", "&coff"));

        Entry<String, String> dropSwitch = new AbstractMap.SimpleEntry<>(dropSwitchEnabled, dropSwitchDisabled);

        MenuItemParser menuItemParser = new MenuItemParser(this.logger, this.itemCache);

        List<MenuItem> menuItems = menuItemParser.parseMany(section.getConfigurationSection("items"));

        return new Menu(title, rows, filler, dropSwitch, menuItems);
    }

}
