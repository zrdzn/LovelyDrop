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
package io.github.zrdzn.minecraft.lovelydrop.menu;

import io.github.zrdzn.minecraft.lovelydrop.item.Item;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MenuItem {

    private final MaterialData type;
    private final String displayName;
    private final List<String> lore;
    private final boolean showEnchantments;
    private final Map<ClickType, MenuAction> action;
    private final Entry<Integer, Integer> slot;
    private final Item dropItem;

    public MenuItem(MaterialData type, String displayName, List<String> lore, boolean showEnchantments,
                    Map<ClickType, MenuAction> action, Entry<Integer, Integer> slot, Item dropItem) {
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

    public Item getDropItem() {
        return this.dropItem;
    }

}
