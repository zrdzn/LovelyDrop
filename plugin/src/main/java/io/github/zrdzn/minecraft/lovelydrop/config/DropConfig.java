package io.github.zrdzn.minecraft.lovelydrop.config;

import eu.okaeri.commons.range.IntRange;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ComplexItemStack;
import io.github.zrdzn.minecraft.lovelydrop.serdes.FloatFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public class DropConfig extends OkaeriConfig {

    public static class FortuneConfig extends OkaeriConfig {

        @Comment("Percentage chance of dropping the item.")
        public FloatFormat chance;

        @Comment("")
        @Comment("Range of the amount of items that can be dropped.")
        public IntRange amount;

        @Comment("")
        @Comment("Amount of experience given to the player when they have a successful drop.")
        public int experience;

        public FortuneConfig(float chance, IntRange amount, int experience) {
            this(new FloatFormat(chance), amount, experience);
        }

        public FortuneConfig(FloatFormat chance, IntRange amount, int experience) {
            this.chance = chance;
            this.amount = amount;
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
    public ComplexItemStack item;

    @Comment("")
    @Comment("Item representation of the block from which 'item' should be dropped.")
    @Comment("If you want to use legacy materials, set a durability to some number, e.g. material: stone with durability: 5 for andesite.")
    @Comment("Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for list of materials.")
    private Set<ItemStack> sources;

    @Comment("")
    @Comment("Height range within which the item can be dropped.")
    public IntRange height;

    @Comment("")
    @Comment("All properties will be applied to the drop, depending on the current pickaxe fortune level.")
    @Comment("It must contain at least one section.")
    public Map<Integer, FortuneConfig> fortune;

    @Comment("")
    @Comment("List of biomes in which the drop will not be dropped.")
    public Set<Biome> disabledBioms;

    public DropConfig(ComplexItemStack item, ItemStack source, IntRange height,
            Map<Integer, FortuneConfig> fortune) {
        this(item, new HashSet<>() {
            {
                this.add(source);
            }
        }, height, fortune, new HashSet<>());
    }

    public DropConfig(ComplexItemStack item, Set<ItemStack> sources, IntRange height,
            Map<Integer, FortuneConfig> fortune) {
        this(item, sources, height, fortune, new HashSet<>());
    }

    public DropConfig(ComplexItemStack item, Set<ItemStack> sources, IntRange height,
            Map<Integer, FortuneConfig> fortune, Set<Biome> disabledBioms) {
        this.item = item;
        this.sources = sources;
        this.height = height;
        this.fortune = fortune;
        this.disabledBioms = disabledBioms;
    }

    public Set<ItemStack> getSources() {
        return sources.stream().map(ItemStack::clone).collect(Collectors.toSet());
    }

    public void setSources(Set<ItemStack> sources) {
        this.sources = sources;
    }
}
