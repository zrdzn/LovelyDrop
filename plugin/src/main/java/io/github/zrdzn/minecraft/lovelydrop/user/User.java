package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItem;

public class User {

    private final UUID id;
    private final Set<DropItem> disabledDrops;
    private final Map<DropItem, Boolean> inventoryDrops;

    public User(UUID id) {
        this.id = id;
        this.disabledDrops = new HashSet<>();
        this.inventoryDrops = new HashMap<>();
    }

    public UUID getId() {
        return this.id;
    }

    public void disableDrop(DropItem dropItem) {
        this.disabledDrops.add(dropItem);
    }

    public void enableDrop(DropItem dropItem) {
        this.disabledDrops.remove(dropItem);
    }

    public boolean hasDisabledDrop(DropItem dropItem) {
        return this.disabledDrops.contains(dropItem);
    }

    public Set<DropItem> getDisabledDrops() {
        return this.disabledDrops;
    }

    public void addInventoryDrop(DropItem dropItem, boolean initialValue) {
        this.inventoryDrops.put(dropItem, initialValue);
    }

    public void switchInventoryDrop(String itemId, boolean newValue) throws IllegalArgumentException {
        DropItem dropItem = this.inventoryDrops.keySet().stream()
            .filter(key -> key.getId().equals(itemId))
            .findAny()
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("Drop with the specified item id does not exist (%s).",
                    itemId)));

        this.inventoryDrops.replace(dropItem, newValue);
    }

    public boolean hasSwitchedInventoryDrop(String itemId) throws IllegalArgumentException {
        return this.inventoryDrops.entrySet().stream()
            .filter((entry) -> entry.getKey().getId().equals(itemId))
            .findAny()
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("Drop with the specified item id does not exist (%s).",
                    itemId)))
            .getValue();
    }

}
