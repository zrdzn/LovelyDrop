package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;

public class Menu {

    private final String title;
    private final int rows;
    private final ItemStack filler;
    private final Entry<String, String> dropSwitch;
    private final Entry<String, String> inventoryDropSwitch;
    private final Entry<String, String> amountFormat;
    private final Entry<String, String> heightFormat;
    private final boolean defaultDropToInventory;
    private final List<MenuItem> items;

    public Menu(String title, int rows, ItemStack filler, Entry<String, String> dropSwitch,
                Entry<String, String> inventoryDropSwitch, Entry<String, String> amountFormat,
                Entry<String, String> heightFormat, boolean defaultDropToInventory, List<MenuItem> items) {
        this.title = title;
        this.rows = rows;
        this.filler = filler;
        this.dropSwitch = dropSwitch;
        this.inventoryDropSwitch = inventoryDropSwitch;
        this.amountFormat = amountFormat;
        this.heightFormat = heightFormat;
        this.defaultDropToInventory = defaultDropToInventory;
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

    public boolean isDefaultDropToInventory() {
        return this.defaultDropToInventory;
    }

    public List<MenuItem> getItems() {
        return this.items;
    }

}
