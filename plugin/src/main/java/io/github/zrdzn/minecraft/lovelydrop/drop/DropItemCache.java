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
package io.github.zrdzn.minecraft.lovelydrop.drop;

import org.bukkit.material.MaterialData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
