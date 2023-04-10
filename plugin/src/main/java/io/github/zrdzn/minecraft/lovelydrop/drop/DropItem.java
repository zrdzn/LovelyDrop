package io.github.zrdzn.minecraft.lovelydrop.drop;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

public class DropItem {

    private final String id;
    private final MaterialData type;
    private final MaterialData source;
    private final Map<Integer, DropProperty> properties;
    private final Entry<Integer, Integer> height;
    private final String displayName;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantments;

    public DropItem(String id, MaterialData type, MaterialData source, Map<Integer, DropProperty> properties,
                    Entry<Integer, Integer> height, String displayName, List<String> lore,
                    Map<Enchantment, Integer> enchantments) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.properties = properties;
        this.height = height;
        this.displayName = displayName;
        this.lore = lore;
        this.enchantments = enchantments;
    }

    public String getId() {
        return this.id;
    }

    public MaterialData getType() {
        return this.type;
    }

    public MaterialData getSource() {
        return this.source;
    }

    public Map<Integer, DropProperty> getProperties() {
        return this.properties;
    }

    public Entry<Integer, Integer> getHeight() {
        return this.height;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

}
