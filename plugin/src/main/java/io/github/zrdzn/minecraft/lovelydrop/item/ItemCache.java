package io.github.zrdzn.minecraft.lovelydrop.item;

import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ItemCache {

    private final Map<Material, Set<Item>> drops = new HashMap<>();

    public void addDrop(Item item) {
        this.drops.computeIfAbsent(item.getSource(), key -> new HashSet<>()).add(item);
    }

    public void removeDrops(Material source) {
        this.drops.remove(source);
    }

    public Optional<Item> getDrop(String dropId) {
        for (Set<Item> dropItems : this.drops.values()) {
            for (Item dropItem : dropItems) {
                if (dropItem.getId().equals(dropId)) {
                    return Optional.of(dropItem);
                }
            }
        }

        return Optional.empty();
    }

    public Set<Item> getDrops(Material source) {
        Set<Item> items = this.drops.getOrDefault(source, Collections.emptySet());
        return items.isEmpty() ? items : new HashSet<>(items);
    }

    public Map<Material, Set<Item>> getDrops() {
        return this.drops;
    }

}
