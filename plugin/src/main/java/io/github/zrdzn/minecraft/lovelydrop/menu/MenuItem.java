package io.github.zrdzn.minecraft.lovelydrop.menu;

import io.github.zrdzn.minecraft.lovelydrop.item.Item;
import org.bukkit.Material;

import java.util.List;
import java.util.Map.Entry;

public class MenuItem {

    private final Material type;
    private final String displayName;
    private final List<String> lore;
    private final MenuAction action;
    private final Entry<Integer, Integer> slot;
    private final Item dropItem;

    public MenuItem(Material type, String displayName, List<String> lore, MenuAction action,
                    Entry<Integer, Integer> slot, Item dropItem) {
        this.type = type;
        this.displayName = displayName;
        this.lore = lore;
        this.action = action;
        this.slot = slot;
        this.dropItem = dropItem;
    }

    public Material getType() {
        return this.type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public MenuAction getAction() {
        return this.action;
    }

    public Entry<Integer, Integer> getSlot() {
        return this.slot;
    }

    public Item getDropItem() {
        return this.dropItem;
    }

}
