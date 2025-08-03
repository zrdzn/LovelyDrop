package io.github.zrdzn.minecraft.lovelydrop.menu;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.util.Map;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MenuItemConfig extends OkaeriConfig {

    public static class SlotConfig extends OkaeriConfig {

        @Comment("Row of the menu item.")
        public int row;

        @Comment("")
        @Comment("Column of the menu item.")
        public int column;

        public SlotConfig(int row, int column) {
            this.row = row;
            this.column = column;
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
    public ItemStack item;

    @Comment("")
    @Comment("What should happen when the player clicks the item.")
    @Comment("Available options:")
    @Comment("NONE - nothing happens")
    @Comment("CLOSE_MENU - closes the menu")
    @Comment("SWITCH_DROP - switches the drop (id of the menu item must be equal to drop item id).")
    @Comment("SWITCH_DROP_TO_INVENTORY - switches the drop to inventory (id of the menu item must be equal to drop item id).")
    @Comment(" ")
    @Comment("Section contains key and value fields where the key is the specified field in the https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/ClickType.html.")
    public Map<ClickType, MenuAction> clickAction;

    @Comment("")
    @Comment("Item position in the menu.")
    public SlotConfig slot;

    public MenuItemConfig(ItemStack item, Map<ClickType, MenuAction> clickAction, SlotConfig slot) {
        this.item = item;
        this.clickAction = clickAction;
        this.slot = slot;
    }
}
