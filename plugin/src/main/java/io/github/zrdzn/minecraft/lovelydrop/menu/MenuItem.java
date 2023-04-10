package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItem;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;

public class MenuItem {

    private final MaterialData type;
    private final String displayName;
    private final List<String> lore;
    private final boolean showEnchantments;
    private final Map<ClickType, MenuAction> action;
    private final Entry<Integer, Integer> slot;
    private final DropItem dropItem;

    public MenuItem(MaterialData type, String displayName, List<String> lore, boolean showEnchantments,
                    Map<ClickType, MenuAction> action, Entry<Integer, Integer> slot, DropItem dropItem) {
        this.type = type;
        this.displayName = displayName;
        this.lore = lore;
        this.showEnchantments = showEnchantments;
        this.action = action;
        this.slot = slot;
        this.dropItem = dropItem;
    }

    public MaterialData getType() {
        return this.type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public boolean isShowEnchantments() {
        return this.showEnchantments;
    }

    public Map<ClickType, MenuAction> getAction() {
        return this.action;
    }

    public Entry<Integer, Integer> getSlot() {
        return this.slot;
    }

    public DropItem getDropItem() {
        return this.dropItem;
    }

}
