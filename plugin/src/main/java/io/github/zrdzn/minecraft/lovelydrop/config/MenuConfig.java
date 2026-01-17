package io.github.zrdzn.minecraft.lovelydrop.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuAction;
import io.github.zrdzn.minecraft.lovelydrop.config.MenuItemConfig.SlotConfig;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ColoredText;
import io.github.zrdzn.minecraft.lovelydrop.shared.ItemFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MenuConfig extends OkaeriConfig {

    public static class FillerConfig extends OkaeriConfig {

        @Comment("Whether empty slots should be filled with specified item or not.")
        public boolean enabled = false;

        @Comment("")
        @Comment("Item representation of the drop.")
        @Comment("If you want to use legacy materials, set a durability to some number, e.g. material: stone with durability: 5 for andesite.")
        @Comment("Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for list of materials.")
        @Comment(" ")
        @Comment("Enchantments may vary across different server versions.")
        @Comment("Please verify the differences yourself depending on the version you are using.")
        @Comment("For example:")
        @Comment("1.8  - damage_all  |  1.8  - durability  |  1.8  - loot_bonus_mobs")
        @Comment("1.18 - sharpness   |  1.18 - unbreaking  |  1.18 - loot")
        public ItemStack item = new ItemStack(Material.GLASS);
    }

    public static class SwitchConfig extends OkaeriConfig {

        @Comment("Used when the switch is enabled.")
        public ColoredText enabled = new ColoredText("&eon");

        @Comment("")
        @Comment("Used when the switch is disabled.")
        public ColoredText disabled = new ColoredText("&coff");
    }

    @Comment("Title of the drop menu.")
    public ColoredText title = new ColoredText("&2&lDrop Menu");

    @Comment("")
    @Comment("Rows amount of the drop menu.")
    public int rows = 4;

    @Comment("")
    @Comment("Item that is filled in empty slots.")
    public FillerConfig filler = new FillerConfig();

    @Comment("")
    @Comment("Text placeholder which stands for {SWITCH} that could be used in items section.")
    public SwitchConfig dropSwitch = new SwitchConfig();

    @Comment("")
    @Comment("Text placeholder which stands for {SWITCH_INVENTORY} that could be used in items section.")
    public SwitchConfig inventoryDropSwitch = new SwitchConfig();

    @Comment("")
    @Comment("Text placeholder which stands for {AMOUNT-{LEVEL}} that could be used in items section.")
    public IntRangeFormatConfig amountFormat =
            new IntRangeFormatConfig("&e{AMOUNT}", "&e{AMOUNT-MIN}&8-&e{AMOUNT-MAX}");

    @Comment("")
    @Comment("Text placeholder which stands for {HEIGHT} that could be used in items section.")
    public IntRangeFormatConfig heightFormat =
            new IntRangeFormatConfig("&e{HEIGHT}", "&e{HEIGHT-MIN}&8-&e{HEIGHT-MAX}");

    @Comment("")
    @Comment("If drop to inventory should be enabled by default or not.")
    public boolean defaultDropToInventory = true;

    @Comment("")
    @Comment("All items that will appear in the menu.")
    @Comment("Available placeholders ({LEVEL} placeholder stands for fortune enchantment level, 0 is when there is no fortune):")
    @Comment("{CHANCE-{LEVEL}} - a chance of the item to drop")
    @Comment("{AMOUNT-{LEVEL}} - a formatted text that will display amount/s of the drop, it can be configured in the amountFormat section")
    @Comment("{EXPERIENCE-{LEVEL}} - an experience that will be given to the player on the successful drop")
    @Comment("{SWITCH} - a text that will be shown depending on the drop switch status")
    @Comment("{SWITCH_INVENTORY} - a text that will be shown depending on the inventory drop switch status")
    @Comment("{HEIGHT} - a text that will be shown depending on the customized height range")
    public Map<String, MenuItemConfig> items = new HashMap<>() {
        {
            this.put("close",
                    new MenuItemConfig(
                            ItemFactory
                                    .createItem(Material.BARRIER, "&c&lClose",
                                            Collections.singletonList("&7Click to close the menu."))
                                    .getItemStack(),
                            Collections.singletonMap(ClickType.LEFT, MenuAction.CLOSE_MENU),
                            new SlotConfig(4, 5)));

            this.put("diamondSword", new MenuItemConfig(ItemFactory
                    .createItem(Material.DIAMOND_SWORD, "&a&lEpic Sword", new ArrayList<>() {
                        {
                            this.add(" &6Chance: &e{CHANCE-0}%");
                            this.add(" &6Amount: {AMOUNT-0}");
                            this.add(" &6Experience: &e{EXPERIENCE-0}");
                            this.add(" &6Height: {HEIGHT}");
                            this.add(" &6Status: {SWITCH}");
                            this.add(" &6To inventory: {SWITCH_INVENTORY}");
                            this.add("");
                            this.add(" &6Fortune Level:");
                            this.add("   &aLevel 1:");
                            this.add("     &6Chance: &e{CHANCE-1}%");
                            this.add("     &6Amount: {AMOUNT-1}");
                            this.add("     &6Experience: &e{EXPERIENCE-1}");
                            this.add("   &aLevel 2:");
                            this.add("     &6Chance: &e{CHANCE-2}%");
                            this.add("     &6Amount: {AMOUNT-2}");
                            this.add("     &6Experience: &e{EXPERIENCE-2}");
                            this.add("   &aLevel 3:");
                            this.add("     &6Chance: &e{CHANCE-3}%");
                            this.add("     &6Amount: {AMOUNT-3}");
                            this.add("     &6Experience: &e{EXPERIENCE-3}");
                            this.add("");
                            this.add("&8-------------------------------------");
                            this.add(" &aLeft click to switch the drop.");
                            this.add(" &eRight click to switch the inventory drop.");
                            this.add("&8-------------------------------------");
                        }
                    }).getItemStack(), new HashMap<>() {
                        {
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }
                    }, new SlotConfig(1, 5)));

            List<String> sharedLore = new ArrayList<>() {
                {
                    this.add(" &6Chance: &e{CHANCE-0}%");
                    this.add(" &6Amount: {AMOUNT-0}");
                    this.add(" &6Experience: &e{EXPERIENCE-0}");
                    this.add(" &6Height: {HEIGHT}");
                    this.add(" &6Status: {SWITCH}");
                    this.add(" &6To inventory: {SWITCH_INVENTORY}");
                    this.add("");
                    this.add("&8-------------------------------------");
                    this.add(" &aLeft click to switch the drop.");
                    this.add(" &eRight click to switch the inventory drop.");
                    this.add("&8-------------------------------------");
                }
            };

            this.put("gold",
                    new MenuItemConfig(ItemFactory
                            .createItem(Material.GOLD_INGOT, "&e&lGold", sharedLore).getItemStack(),
                            new HashMap<>() {
                                {
                                    this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                                    this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                                }
                            }, new SlotConfig(2, 3)));

            this.put("diamond",
                    new MenuItemConfig(ItemFactory
                            .createItem(Material.DIAMOND, "&b&lDiamond", sharedLore).getItemStack(),
                            new HashMap<>() {
                                {
                                    this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                                    this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                                }
                            }, new SlotConfig(2, 4)));

            this.put("tnt", new MenuItemConfig(
                    ItemFactory.createItem(Material.TNT, "&c&lTNT", sharedLore).getItemStack(),
                    new HashMap<>() {
                        {
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }
                    }, new SlotConfig(2, 5)));

            this.put("emerald",
                    new MenuItemConfig(ItemFactory
                            .createItem(Material.EMERALD, "&a&lEmerald", sharedLore).getItemStack(),
                            new HashMap<>() {
                                {
                                    this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                                    this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                                }
                            }, new SlotConfig(2, 6)));

            this.put("obsidian",
                    new MenuItemConfig(
                            ItemFactory.createItem(Material.OBSIDIAN, "&7&lObsidian", sharedLore)
                                    .getItemStack(),
                            new HashMap<>() {
                                {
                                    this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                                    this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                                }
                            }, new SlotConfig(2, 7)));
        }
    };
}
