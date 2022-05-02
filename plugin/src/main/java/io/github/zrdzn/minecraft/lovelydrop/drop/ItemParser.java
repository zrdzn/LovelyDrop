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
package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import io.github.zrdzn.minecraft.lovelydrop.ParserHelper;
import io.github.zrdzn.minecraft.spigot.EnchantmentMatcher;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ItemParser {

    private final Logger logger;
    private final EnchantmentMatcher enchantmentMatcher;

    public ItemParser(Logger logger, EnchantmentMatcher enchantmentMatcher) {
        this.logger = logger;
        this.enchantmentMatcher = enchantmentMatcher;
    }

    public DropItem parse(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        String typeRaw = section.getString("type");
        if (typeRaw == null) {
            throw new InvalidConfigurationException("Key 'type' is null.");
        }

        MaterialData type = ParserHelper.parseLegacyMaterial(typeRaw);

        String sourceTypeRaw = section.getString("source");
        if (sourceTypeRaw == null) {
            throw new InvalidConfigurationException("Key 'source' is null.");
        }

        MaterialData sourceType = ParserHelper.parseLegacyMaterial(sourceTypeRaw);

        double chance = section.getDouble("chance");
        if (chance <= 0.0D) {
            throw new InvalidConfigurationException("Key 'chance' cannot be 0 and lower.");
        }

        String amountRaw = section.getString("amount");
        if (amountRaw == null) {
            throw new InvalidConfigurationException("Key 'amount' is null.");
        }

        Entry<Integer, Integer> amounts = ParserHelper.parseRange(amountRaw, false);

        int experience = section.getInt("experience");
        if (experience < 0 ) {
            throw new InvalidConfigurationException("Key 'experience' cannot be lower than 0.");
        }

        String heightRaw = section.getString("height");
        if (heightRaw == null) {
            throw new InvalidConfigurationException("Key 'height' is null.");
        }

        Entry<Integer, Integer> height = ParserHelper.parseRange(heightRaw, true);

        String displayNameRaw = section.getString("meta.displayname");

        String displayName;
        if (displayNameRaw == null) {
            displayName = null;
        } else {
            displayName = LovelyDropPlugin.color(displayNameRaw);
        }

        List<String> lore = LovelyDropPlugin.color(section.getStringList("meta.lore"));

        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String enchantmentRaw : section.getStringList("meta.enchantments")) {
            String[] enchantmentRawArray = enchantmentRaw.split(":");

            Enchantment enchantment = this.enchantmentMatcher.matchEnchantment(enchantmentRawArray[0])
                .orElseThrow(() -> new InvalidConfigurationException("Key in 'enchantments' is an invalid enchantment."));

            int level;
            try {
                level = Integer.parseUnsignedInt(enchantmentRawArray[1]);
            } catch (NumberFormatException exception) {
                throw new InvalidConfigurationException("Key in 'enchantments' is an invalid enchantment level.");
            }

            enchantments.put(enchantment, level);
        }

        return new DropItem(section.getName(), type, sourceType, chance, amounts, experience, height, displayName, lore,
            enchantments);
    }

    public List<DropItem> parseMany(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("drops")) {
            throw new InvalidConfigurationException("Provided section is not 'drops'.");
        }

        List<DropItem> dropItems = new ArrayList<>();

        // Add each item section to items list.
        section.getKeys(false).forEach(key -> {
            try {
                dropItems.add(this.parse(section.getConfigurationSection(key)));
            } catch (InvalidConfigurationException exception) {
                this.logger.severe("Something went wrong while parsing item section.");
                exception.printStackTrace();
            }
        });

        return dropItems;
    }

}
