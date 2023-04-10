package io.github.zrdzn.minecraft.lovelydrop.drop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import io.github.zrdzn.minecraft.lovelydrop.ParserHelper;
import io.github.zrdzn.minecraft.spigot.EnchantmentMatcher;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

public class DropItemParser {

    private final Logger logger;
    private final EnchantmentMatcher enchantmentMatcher;

    public DropItemParser(Logger logger, EnchantmentMatcher enchantmentMatcher) {
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

        ConfigurationSection fortunes = section.getConfigurationSection("fortune");
        if (fortunes == null) {
            throw new InvalidConfigurationException("Section 'fortune' must exist and have at least 1 section.");
        }

        Map<Integer, DropProperty> properties = new HashMap<>();
        for (String levelRaw : fortunes.getKeys(false)) {
            int level;
            try {
                level = Integer.parseUnsignedInt(levelRaw);
            } catch (NumberFormatException exception) {
                level = 0;
            }

            ConfigurationSection fortune = fortunes.getConfigurationSection(levelRaw);

            double chance = fortune.getDouble("chance");
            if (chance <= 0.0D) {
                throw new InvalidConfigurationException("Key 'chance' cannot be 0 and lower.");
            }

            String amountRaw = fortune.getString("amount");
            if (amountRaw == null) {
                throw new InvalidConfigurationException("Key 'amount' is null.");
            }

            Entry<Integer, Integer> amounts = ParserHelper.parseRange(amountRaw, false);

            int experience = fortune.getInt("experience");
            if (experience < 0) {
                throw new InvalidConfigurationException("Key 'experience' cannot be lower than 0.");
            }

            properties.put(level, new DropProperty(chance, amounts, experience));
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

            Enchantment enchantment = this.enchantmentMatcher.matchEnchantment(enchantmentRawArray[0]).orElseThrow(() ->
                new InvalidConfigurationException("Key in 'enchantments' is an invalid enchantment."));

            int level;
            try {
                level = Integer.parseUnsignedInt(enchantmentRawArray[1]);
            } catch (NumberFormatException exception) {
                throw new InvalidConfigurationException("Key in 'enchantments' is an invalid enchantment level.");
            }

            enchantments.put(enchantment, level);
        }

        return new DropItem(section.getName(), type, sourceType, properties, height, displayName, lore, enchantments);
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
