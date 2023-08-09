package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuItemConfig.SlotConfig;
import io.github.zrdzn.minecraft.lovelydrop.shared.IntRangeFormatConfig;
import io.github.zrdzn.minecraft.lovelydrop.shared.ItemFactory;
import io.github.zrdzn.minecraft.lovelydrop.transformer.ColoredText;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MenuConfig extends OkaeriConfig {

    public static class FillerConfig extends OkaeriConfig {

        @Comment("Whether empty slots should be filled with specified item or not.")
        private boolean enabled = false;

        @Comment(" ")
        @Comment("Item representation of the drop.")
        @Comment("If you want to use legacy materials, set a durability to some number, e.g. material: stone with durability: 5 for andesite.")
        @Comment("Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for list of materials.")
        @Comment(" ")
        @Comment("Enchantments may vary across different server versions.")
        @Comment("Please verify the differences yourself depending on the version you are using.")
        @Comment("For example:")
        @Comment("1.8  - damage_all  |  1.8  - durability  |  1.8  - loot_bonus_mobs")
        @Comment("1.18 - sharpness   |  1.18 - unbreaking  |  1.18 - loot")
        private ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public ItemStack getItem() {
            return this.item;
        }

        public void setItem(ItemStack item) {
            this.item = item;
        }

    }

    public static class SwitchConfig extends OkaeriConfig {

        @Comment("Used when the switch is enabled.")
        private ColoredText enabled = new ColoredText("&eon");

        @Comment(" ")
        @Comment("Used when the switch is disabled.")
        private ColoredText disabled = new ColoredText("&coff");

        public ColoredText getEnabled() {
            return this.enabled;
        }

        public void setEnabled(ColoredText enabled) {
            this.enabled = enabled;
        }

        public ColoredText getDisabled() {
            return this.disabled;
        }

        public void setDisabled(ColoredText disabled) {
            this.disabled = disabled;
        }

    }

    @Comment("Title of the drop menu.")
    private ColoredText title = new ColoredText("&2&lDrop Menu");

    @Comment(" ")
    @Comment("Rows amount of the drop menu.")
    private int rows = 4;

    @Comment(" ")
    @Comment("Item that is filled in empty slots.")
    private FillerConfig filler = new FillerConfig();

    @Comment(" ")
    @Comment("Text placeholder which stands for {SWITCH} that could be used in items section.")
    private SwitchConfig dropSwitch = new SwitchConfig();

    @Comment(" ")
    @Comment("Text placeholder which stands for {SWITCH_INVENTORY} that could be used in items section.")
    private SwitchConfig inventoryDropSwitch = new SwitchConfig();

    @Comment(" ")
    @Comment("Text placeholder which stands for {AMOUNT-{LEVEL}} that could be used in items section.")
    private IntRangeFormatConfig amountFormat = new IntRangeFormatConfig("&e{AMOUNT}", "&e{AMOUNT-MIN}&8-&e{AMOUNT-MAX}");

    @Comment(" ")
    @Comment("Text placeholder which stands for {HEIGHT} that could be used in items section.")
    private IntRangeFormatConfig heightFormat = new IntRangeFormatConfig("&e{HEIGHT}", "&e{HEIGHT-MIN}&8-&e{HEIGHT-MAX}");

    @Comment(" ")
    @Comment("If drop to inventory should be enabled by default or not.")
    private boolean defaultDropToInventory = true;

    @Comment(" ")
    @Comment("All items that will appear in the menu.")
    @Comment("Available placeholders ({LEVEL} placeholder stands for fortune enchantment level, 0 is when there is no fortune):")
    @Comment("{CHANCE-{LEVEL}} - a chance of the item to drop")
    @Comment("{AMOUNT-{LEVEL}} - a formatted text that will display amount/s of the drop, it can be configured in the amountFormat section")
    @Comment("{EXPERIENCE-{LEVEL}} - an experience that will be given to the player on the successful drop")
    @Comment("{SWITCH} - a text that will be shown depending on the drop switch status")
    @Comment("{SWITCH_INVENTORY} - a text that will be shown depending on the inventory drop switch status")
    @Comment("{HEIGHT} - a text that will be shown depending on the customized height range")
    private Map<String, MenuItemConfig> items = new HashMap<String, MenuItemConfig>() {{
        this.put(
                "close",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.BARRIER, "&c&lClose", Collections.singletonList("&7Click to close the menu.")),
                        Collections.singletonMap(ClickType.LEFT, MenuAction.CLOSE_MENU),
                        new SlotConfig(4, 5)
                )
        );

        this.put(
                "diamondSword",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.DIAMOND_SWORD, "&a&lEpic Sword", new ArrayList<String>() {{
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
                        }}),
                        new HashMap<ClickType, MenuAction>() {{
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }},
                        new SlotConfig(1, 5)
                )
        );

        List<String> sharedLore = new ArrayList<String>() {{
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
        }};

        this.put(
                "gold",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.GOLD_INGOT, "&e&lGold", sharedLore),
                        new HashMap<ClickType, MenuAction>() {{
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }},
                        new SlotConfig(2, 3)
                )
        );

        this.put(
                "diamond",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.DIAMOND, "&b&lDiamond", sharedLore),
                        new HashMap<ClickType, MenuAction>() {{
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }},
                        new SlotConfig(2, 4)
                )
        );

        this.put(
                "tnt",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.TNT, "&c&lTNT", sharedLore),
                        new HashMap<ClickType, MenuAction>() {{
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }},
                        new SlotConfig(2, 5)
                )
        );

        this.put(
                "emerald",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.EMERALD, "&a&lEmerald", sharedLore),
                        new HashMap<ClickType, MenuAction>() {{
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }},
                        new SlotConfig(2, 6)
                )
        );

        this.put(
                "obsidian",
                new MenuItemConfig(
                        ItemFactory.createItem(Material.OBSIDIAN, "&7&lObsidian", sharedLore),
                        new HashMap<ClickType, MenuAction>() {{
                            this.put(ClickType.LEFT, MenuAction.SWITCH_DROP);
                            this.put(ClickType.RIGHT, MenuAction.SWITCH_DROP_TO_INVENTORY);
                        }},
                        new SlotConfig(2, 7)
                )
        );
    }};

    public ColoredText getTitle() {
        return this.title;
    }

    public void setTitle(ColoredText title) {
        this.title = title;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public FillerConfig getFiller() {
        return this.filler;
    }

    public void setFiller(FillerConfig filler) {
        this.filler = filler;
    }

    public SwitchConfig getDropSwitch() {
        return this.dropSwitch;
    }

    public void setDropSwitch(SwitchConfig dropSwitch) {
        this.dropSwitch = dropSwitch;
    }

    public SwitchConfig getInventoryDropSwitch() {
        return this.inventoryDropSwitch;
    }

    public void setInventoryDropSwitch(SwitchConfig inventoryDropSwitch) {
        this.inventoryDropSwitch = inventoryDropSwitch;
    }

    public IntRangeFormatConfig getAmountFormat() {
        return this.amountFormat;
    }

    public void setAmountFormat(IntRangeFormatConfig amountFormat) {
        this.amountFormat = amountFormat;
    }

    public IntRangeFormatConfig getHeightFormat() {
        return this.heightFormat;
    }

    public void setHeightFormat(IntRangeFormatConfig heightFormat) {
        this.heightFormat = heightFormat;
    }

    public boolean isDefaultDropToInventory() {
        return this.defaultDropToInventory;
    }

    public void setDefaultDropToInventory(boolean defaultDropToInventory) {
        this.defaultDropToInventory = defaultDropToInventory;
    }

    public Map<String, MenuItemConfig> getItems() {
        return this.items;
    }

    public void setItems(Map<String, MenuItemConfig> items) {
        this.items = items;
    }

}
