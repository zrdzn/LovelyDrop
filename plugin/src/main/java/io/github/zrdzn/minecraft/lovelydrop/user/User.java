/*
 * Copyright (c) 2022 zrdzn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.zrdzn.minecraft.lovelydrop.user;

import io.github.zrdzn.minecraft.lovelydrop.drop.DropItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
                new IllegalArgumentException(String.format("Drop with the specified item id does not exist (%s).", itemId)));

        this.inventoryDrops.replace(dropItem, newValue);
    }

    public boolean hasSwitchedInventoryDrop(String itemId) throws IllegalArgumentException {
        return this.inventoryDrops.entrySet().stream()
            .filter((entry) -> entry.getKey().getId().equals(itemId))
            .findAny()
            .orElseThrow(() ->
                new IllegalArgumentException(String.format("Drop with the specified item id does not exist (%s).", itemId)))
            .getValue();
    }

}
