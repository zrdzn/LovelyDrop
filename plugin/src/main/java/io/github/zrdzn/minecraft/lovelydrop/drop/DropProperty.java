package io.github.zrdzn.minecraft.lovelydrop.drop;

import java.util.Map.Entry;

public class DropProperty {

    private final double chance;
    private final Entry<Integer, Integer> amount;
    private final int experience;

    public DropProperty(double chance, Entry<Integer, Integer> amount, int experience) {
        this.chance = chance;
        this.amount = amount;
        this.experience = experience;
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

}
