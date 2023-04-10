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

import java.util.Map.Entry;

public class DropProperty {

    private final double chance;
    private final String formattedChance;
    private final Entry<Integer, Integer> amount;
    private final int experience;

    public DropProperty(double chance, String formattedChance, Entry<Integer, Integer> amount, int experience) {
        this.chance = chance;
        this.formattedChance = formattedChance;
        this.amount = amount;
        this.experience = experience;
    }

    public double getChance() {
        return this.chance;
    }

    public String getFormattedChance() {
        return this.formattedChance;
    }

    public Entry<Integer, Integer> getAmount() {
        return this.amount;
    }

    public int getExperience() {
        return this.experience;
    }

}
