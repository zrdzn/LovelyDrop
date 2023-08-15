package io.github.zrdzn.minecraft.lovelydrop;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import eu.okaeri.commons.range.IntRange;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropConfig;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropConfig.FortuneConfig;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageConfig;
import io.github.zrdzn.minecraft.lovelydrop.shared.ItemFactory;
import io.github.zrdzn.minecraft.lovelydrop.storage.StorageConfig;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class PluginConfig extends OkaeriConfig {

    @Comment("How often should all users settings be saved to the database (in seconds)?")
    @Comment("By default it is 180 seconds (3 minutes).")
    private long userSettingsSaveInterval = 180L;

    @Comment("")
    @Comment("Whole configuration for the drop menu.")
    private MenuConfig menu = new MenuConfig();

    @Comment("")
    @Comment("All drops that will be available on the server.")
    private Map<String, DropConfig> drops = new HashMap<String, DropConfig>() {{
        this.put(
                "diamondSword",
                new DropConfig(
                        ItemFactory.createItem(
                                Material.DIAMOND_SWORD,
                                "&a&lEpic Sword",
                                Collections.singletonList("&6Legendary diamond sword."),
                                new HashMap<Enchantment, Integer>() {{
                                    this.put(Enchantment.FIRE_ASPECT, 1);
                                    this.put(Enchantment.KNOCKBACK, 1);
                                }},
                                new HashMap<String, Object>() {{
                                    this.put("identifier", "sword-drop");
                                    this.put("number", 5);
                                }}
                        ),
                        new ItemStack(Material.STONE),
                        IntRange.of(0, 256),
                        new HashMap<Integer, FortuneConfig>() {{
                            // No fortune on pickaxe.
                            this.put(
                                    0,
                                    new FortuneConfig(
                                            20F,
                                            IntRange.of(1, 2),
                                            175
                                    )
                            );

                            // Fortune 1.
                            this.put(
                                    1,
                                    new FortuneConfig(
                                            30F,
                                            IntRange.of(3, 3),
                                            200
                                    )
                            );

                            // Fortune 2.
                            this.put(
                                    2,
                                    new FortuneConfig(
                                            45F,
                                            IntRange.of(3, 4),
                                            225
                                    )
                            );

                            // Fortune 3.
                            this.put(
                                    3,
                                    new FortuneConfig(
                                            50F,
                                            IntRange.of(5, 5),
                                            250
                                    )
                            );
                        }},
                        new HashSet<Biome>() {{
                            this.add(Biome.COLD_TAIGA);
                            this.add(Biome.COLD_BEACH);
                        }}
                )
        );

        this.put(
                "gold",
                new DropConfig(
                        ItemFactory.createItem(Material.GOLD_INGOT, "&6Gold", Collections.singletonList("&7Dropped gold.")),
                        new ItemStack(Material.STONE),
                        IntRange.of(-50, 100),
                        new HashMap<Integer, FortuneConfig>() {{
                            this.put(
                                    0,
                                    new FortuneConfig(
                                            60F,
                                            IntRange.of(1, 5),
                                            20
                                    )
                            );
                        }}
                )
        );

        this.put(
                "diamond",
                new DropConfig(
                        ItemFactory.createItem(Material.DIAMOND),
                        new ItemStack(Material.STONE),
                        IntRange.of(-20, 50),
                        new HashMap<Integer, FortuneConfig>() {{
                            this.put(
                                    0,
                                    new FortuneConfig(
                                            50F,
                                            IntRange.of(1, 3),
                                            100
                                    )
                            );
                        }}
                )
        );

        this.put(
                "tnt",
                new DropConfig(
                        ItemFactory.createItem(Material.TNT),
                        new ItemStack(Material.STONE),
                        IntRange.of(0, 256),
                        new HashMap<Integer, FortuneConfig>() {{
                            this.put(
                                    0,
                                    new FortuneConfig(
                                            20F,
                                            IntRange.of(2, 2),
                                            150
                                    )
                            );
                        }}
                )
        );

        this.put(
                "emerald",
                new DropConfig(
                        ItemFactory.createItem(Material.EMERALD),
                        new ItemStack(Material.STONE),
                        IntRange.of(30, 60),
                        new HashMap<Integer, FortuneConfig>() {{
                            this.put(
                                    0,
                                    new FortuneConfig(
                                            10F,
                                            IntRange.of(1, 4),
                                            200
                                    )
                            );
                        }}
                )
        );

        this.put(
                "obsidian",
                new DropConfig(
                        ItemFactory.createItem(Material.OBSIDIAN),
                        new ItemStack(Material.STONE),
                        IntRange.of(0, 256),
                        new HashMap<Integer, FortuneConfig>() {{
                            this.put(
                                    0,
                                    new FortuneConfig(
                                            20F,
                                            IntRange.of(3, 3),
                                            135
                                    )
                            );
                        }}
                )
        );
    }};

    @Comment("")
    private StorageConfig storage = new StorageConfig();

    @Comment("")
    @Comment("All messages that can be sent by plugin to players.")
    @Comment("If you want to disable specific messages just place a '#' before them.")
    private MessageConfig messages = new MessageConfig();

    public long getUserSettingsSaveInterval() {
        return this.userSettingsSaveInterval;
    }

    public void setUserSettingsSaveInterval(long userSettingsSaveInterval) {
        this.userSettingsSaveInterval = userSettingsSaveInterval;
    }

    public MenuConfig getMenu() {
        return this.menu;
    }

    public void setMenu(MenuConfig menu) {
        this.menu = menu;
    }

    public Map<String, DropConfig> getDrops() {
        return this.drops;
    }

    public void setDrops(Map<String, DropConfig> drops) {
        this.drops = drops;
    }

    public StorageConfig getStorage() {
        return this.storage;
    }

    public void setStorage(StorageConfig storage) {
        this.storage = storage;
    }

    public MessageConfig getMessages() {
        return this.messages;
    }

    public void setMessages(MessageConfig messages) {
        this.messages = messages;
    }

}
