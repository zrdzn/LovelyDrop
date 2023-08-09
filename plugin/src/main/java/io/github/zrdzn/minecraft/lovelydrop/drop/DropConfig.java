package io.github.zrdzn.minecraft.lovelydrop.drop;

import java.util.Map;
import eu.okaeri.commons.range.IntRange;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.github.zrdzn.minecraft.lovelydrop.transformer.FloatFormat;
import org.bukkit.inventory.ItemStack;

public class DropConfig extends OkaeriConfig {

    public static class FortuneConfig extends OkaeriConfig {

        @Comment("Percentage chance of dropping the item.")
        private FloatFormat chance;

        @Comment(" ")
        @Comment("Range of the amount of items that can be dropped.")
        private IntRange amount;

        @Comment(" ")
        @Comment("Amount of experience given to the player when they have a successful drop.")
        private int experience;

        public FortuneConfig(float chance, IntRange amount, int experience) {
            this(new FloatFormat(chance), amount, experience);
        }

        public FortuneConfig(FloatFormat chance, IntRange amount, int experience) {
            this.chance = chance;
            this.amount = amount;
            this.experience = experience;
        }

        public FloatFormat getChance() {
            return this.chance;
        }

        public void setChance(FloatFormat chance) {
            this.chance = chance;
        }

        public IntRange getAmount() {
            return this.amount;
        }

        public void setAmount(IntRange amount) {
            this.amount = amount;
        }

        public int getExperience() {
            return this.experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

    }

    @Comment("Item representation of the drop.")
    @Comment("If you want to use legacy materials, set a durability to some number, e.g. material: stone with durability: 5 for andesite.")
    @Comment("Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for list of materials.")
    @Comment(" ")
    @Comment("Enchantments may vary across different server versions.")
    @Comment("Please verify the differences yourself depending on the version you are using.")
    @Comment("For example:")
    @Comment("1.8  - damage_all  |  1.8  - durability  |  1.8  - loot_bonus_mobs")
    @Comment("1.18 - sharpness   |  1.18 - unbreaking  |  1.18 - loot")
    private ItemStack item;

    @Comment(" ")
    @Comment("Item representation of the block from which 'item' should be dropped.")
    @Comment("If you want to use legacy materials, set a durability to some number, e.g. material: stone with durability: 5 for andesite.")
    @Comment("Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for list of materials.")
    private ItemStack source;

    @Comment(" ")
    @Comment("Height range within which the item can be dropped.")
    private IntRange height;

    @Comment(" ")
    @Comment("All properties will be applied to the drop, depending on the current pickaxe fortune level.")
    @Comment("It must contain at least one section.")
    private Map<Integer, FortuneConfig> fortune;

    public DropConfig(ItemStack item, ItemStack source, IntRange height, Map<Integer, FortuneConfig> fortune) {
        this.item = item;
        this.source = source;
        this.height = height;
        this.fortune = fortune;
    }

    public ItemStack getItem() {
        return this.item.clone();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getSource() {
        return this.source.clone();
    }

    public void setSource(ItemStack source) {
        this.source = source;
    }

    public IntRange getHeight() {
        return this.height;
    }

    public void setHeight(IntRange height) {
        this.height = height;
    }

    public Map<Integer, FortuneConfig> getFortune() {
        return this.fortune;
    }

    public void setFortune(Map<Integer, FortuneConfig> fortune) {
        this.fortune = fortune;
    }

}
