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
