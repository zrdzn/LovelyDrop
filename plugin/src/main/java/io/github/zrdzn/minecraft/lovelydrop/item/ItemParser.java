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
package io.github.zrdzn.minecraft.lovelydrop.item;

import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ItemParser {

    private final Logger logger;

    public ItemParser(Logger logger) {
        this.logger = logger;
    }

    public Item parse(ConfigurationSection section) throws InvalidConfigurationException {
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

        String sourceTypeRaw = section.getString("source");
        if (sourceTypeRaw == null) {
            throw new InvalidConfigurationException("Key 'source' is null.");
        }

        Material sourceType = Material.matchMaterial(sourceTypeRaw);
        if (sourceType == null) {
            throw new InvalidConfigurationException("Material with key 'source' does not exist.");
        }

        double chance = section.getDouble("chance");
        if (chance <= 0.0D) {
            throw new InvalidConfigurationException("Key 'chance' cannot be 0 and lower.");
        }

        Entry<Integer, Integer> amounts;

        String amountRaw = section.getString("amount");
        if (amountRaw == null) {
            throw new InvalidConfigurationException("Key 'amount' is null.");
        }

        String[] amountRawArray = amountRaw.split("-");
        if (amountRawArray.length == 0) {
            throw new InvalidConfigurationException("Key 'amount' is empty.");
        } else if (amountRawArray.length == 1) {
            try {
                int amount = Integer.parseUnsignedInt(amountRawArray[0]);
                amounts = new AbstractMap.SimpleEntry<>(amount, amount);
            } catch (NumberFormatException exception) {
                throw new InvalidConfigurationException("Key 'amount' does not contain number.");
            }
        } else {
            try {
                amounts = new AbstractMap.SimpleEntry<>(Integer.parseUnsignedInt(amountRawArray[0]),
                    Integer.parseUnsignedInt(amountRawArray[1]));
            } catch (NumberFormatException exception) {
                throw new InvalidConfigurationException("Key 'amount' is wrongly formatted.");
            }
        }

        int experience = section.getInt("experience");
        if (experience < 0 ) {
            throw new InvalidConfigurationException("Key 'experience' cannot be lower than 0.");
        }

        String displayNameRaw = section.getString("meta.displayname");

        String displayName;
        if (displayNameRaw == null) {
            displayName = null;
        } else {
            displayName = LovelyDropPlugin.color(displayNameRaw);
        }

        List<String> lore = LovelyDropPlugin.color(section.getStringList("meta.lore"));

        return new Item(section.getName(), type, sourceType, chance, amounts, experience, displayName, lore);
    }

    public List<Item> parseMany(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("drops")) {
            throw new InvalidConfigurationException("Provided section is not 'drops'.");
        }

        List<Item> items = new ArrayList<>();

        // Add each item section to items list.
        section.getKeys(false).forEach(key -> {
            try {
                items.add(this.parse(section.getConfigurationSection(key)));
            } catch (InvalidConfigurationException exception) {
                this.logger.severe("Something went wrong while parsing item section.");
                exception.printStackTrace();
            }
        });

        return items;
    }

}
