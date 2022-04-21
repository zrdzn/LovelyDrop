package io.github.zrdzn.minecraft.lovelydrop.item;

import org.bukkit.Material;

import java.util.List;
import java.util.Map.Entry;

public class Item {

    private final String id;
    private final Material type;
    private final Material source;
    private final double chance;
    private final Entry<Integer, Integer> amount;
    private final int experience;
    private final String displayName;
    private final List<String> lore;

    public Item(String id, Material type, Material source, double chance, Entry<Integer, Integer> amount, int experience,
                String displayName, List<String> lore) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.chance = chance;
        this.amount = amount;
        this.experience = experience;
        this.displayName = displayName;
        this.lore = lore;
    }

    public String getId() {
        return this.id;
    }

    public Material getType() {
        return this.type;
    }

    public Material getSource() {
        return this.source;
    }

    public double getChance() {
        return this.chance;
    }

    public Entry<Integer, Integer> getAmount() {
        return this.amount;
    }

    public int getExperience() {
        return this.experience;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

}
