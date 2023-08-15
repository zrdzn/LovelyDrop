package io.github.zrdzn.minecraft.lovelydrop.serdes;

import java.util.Map;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class that holds item stack and its NBT data.
 *
 * @param <T> type of NBT data
 */
public class ComplexItemStack<T> {

    private ItemStack itemStack;
    private Map<String, T> nbtData;

    public ComplexItemStack(ItemStack itemStack, Map<String, T> nbtData) {
        this.itemStack = itemStack;
        this.nbtData = nbtData;
    }

    public ItemStack getItemStack() {
        return this.itemStack.clone();
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Map<String, T> getNbtData() {
        return this.nbtData;
    }

    public void setNbtData(Map<String, T> nbtData) {
        this.nbtData = nbtData;
    }

}
