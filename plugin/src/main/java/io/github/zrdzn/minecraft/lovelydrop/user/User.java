package io.github.zrdzn.minecraft.lovelydrop.user;

import io.github.zrdzn.minecraft.lovelydrop.item.Item;

import java.util.Set;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Set<Item> disabledDrops;

    public User(UUID id, Set<Item> disabledDrops) {
        this.id = id;
        this.disabledDrops = disabledDrops;
    }

    public UUID getId() {
        return this.id;
    }

    public void disableDrop(Item item) {
        this.disabledDrops.add(item);
    }

    public void enableDrop(Item item) {
        this.disabledDrops.remove(item);
    }

    public boolean hasDisabledDrop(Item item) {
        return this.disabledDrops.contains(item);
    }

    public Set<Item> getDisabledDrops() {
        return this.disabledDrops;
    }

}
