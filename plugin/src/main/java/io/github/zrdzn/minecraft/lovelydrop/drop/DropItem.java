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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DropItem {

    private final String id;
    private final MaterialData type;
    private final MaterialData source;
    private final double chance;
    private final Entry<Integer, Integer> amount;
    private final int experience;
    private final Entry<Integer, Integer> height;
    private final String displayName;
    private final List<String> lore;
    private final Map<Enchantment, Integer> enchantments;

    public DropItem(String id, MaterialData type, MaterialData source, double chance, Entry<Integer, Integer> amount,
                    int experience, Entry<Integer, Integer> height, String displayName, List<String> lore,
                    Map<Enchantment, Integer> enchantments) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.chance = chance;
        this.amount = amount;
        this.experience = experience;
        this.height = height;
        this.displayName = displayName;
        this.lore = lore;
        this.enchantments = enchantments;
    }

    public String getId() {
        return this.id;
    }

    public MaterialData getType() {
        return this.type;
    }

    public MaterialData getSource() {
        return this.source;
    }

    public double getChance() {
        return this.chance;
    }

    public Entry<Integer, Integer> getAmount() {
        return this.amount;
    }

    public int getExperience() {
        return this.experience;
    }

    public Entry<Integer, Integer> getHeight() {
        return this.height;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

}
