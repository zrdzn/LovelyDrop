package io.github.zrdzn.minecraft.lovelydrop.drop;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.bukkit.material.MaterialData;

public class DropItemCache {

    private final Map<MaterialData, Set<DropItem>> drops = new HashMap<>();

    public void addDrop(DropItem dropItem) {
        this.drops.computeIfAbsent(dropItem.getSource(), key -> new HashSet<>()).add(dropItem);
    }

    public Optional<DropItem> getDrop(String dropId) {
        return this.drops.values().stream()
            .flatMap(Collection::stream)
            .filter(item -> item.getId().equals(dropId))
            .findFirst();
    }

    public Set<DropItem> getDrops(MaterialData source) {
        Set<DropItem> dropItems = this.drops.getOrDefault(source, Collections.emptySet());
        return dropItems.isEmpty() ? dropItems : new HashSet<>(dropItems);
    }

    public Map<MaterialData, Set<DropItem>> getDrops() {
        return this.drops;
    }

}
