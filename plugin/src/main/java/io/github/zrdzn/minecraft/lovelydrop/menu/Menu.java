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

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class Menu {

    private final String title;
    private final int rows;
    private final ItemStack filler;
    private final Entry<String, String> dropSwitch;
    private final Entry<String, String> inventoryDropSwitch;
    private final Entry<String, String> amountFormat;
    private final Entry<String, String> heightFormat;
    private final List<MenuItem> items;

    public Menu(String title, int rows, ItemStack filler, Entry<String, String> dropSwitch,
                Entry<String, String> inventoryDropSwitch, Entry<String, String> amountFormat,
                Entry<String, String> heightFormat, List<MenuItem> items) {
        this.title = title;
        this.rows = rows;
        this.filler = filler;
        this.dropSwitch = dropSwitch;
        this.inventoryDropSwitch = inventoryDropSwitch;
        this.amountFormat = amountFormat;
        this.heightFormat = heightFormat;
        this.items = items;
    }

    public String getTitle() {
        return this.title;
    }

    public int getRows() {
        return this.rows;
    }

    public Optional<ItemStack> getFiller() {
        return this.filler == null ? Optional.empty() : Optional.of(this.filler.clone());
    }

    public Entry<String, String> getDropSwitch() {
        return this.dropSwitch;
    }

    public Entry<String, String> getInventoryDropSwitch() {
        return this.inventoryDropSwitch;
    }

    public Entry<String, String> getAmountFormat() {
        return this.amountFormat;
    }

    public Entry<String, String> getHeightFormat() {
        return this.heightFormat;
    }

    public List<MenuItem> getItems() {
        return this.items;
    }

}
