package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserSetting {

    private final UUID playerId;
    private final Set<String> disabledDrops;
    private final Map<String, Boolean> dropsToInventory;

    public UserSetting(UUID playerId, Set<String> disabledDrops,
            Map<String, Boolean> dropsToInventory) {
        this.playerId = playerId;
        this.disabledDrops = disabledDrops;
        this.dropsToInventory = dropsToInventory;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public void addDisabledDrop(String dropKey) {
        this.disabledDrops.add(dropKey);
    }

    public boolean hasDisabledDrop(String dropKey) {
        return this.disabledDrops.contains(dropKey);
    }

    public Set<String> getDisabledDrops() {
        return this.disabledDrops;
    }

    public void removeDisabledDrop(String dropKey) {
        this.disabledDrops.remove(dropKey);
    }

    public void setDropToInventory(String dropKey, boolean value) {
        this.dropsToInventory.put(dropKey, value);
    }

    public boolean hasDropToInventory(String dropKey) {
        if (!this.dropsToInventory.containsKey(dropKey)) {
            throw new UserSettingException(
                    "Drop key " + dropKey + " does not exist in user settings cache.");
        }

        return this.dropsToInventory.get(dropKey);
    }

    public Map<String, Boolean> getDropsToInventory() {
        return this.dropsToInventory;
    }
}
