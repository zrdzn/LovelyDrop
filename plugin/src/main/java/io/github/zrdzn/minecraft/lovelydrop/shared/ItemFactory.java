package io.github.zrdzn.minecraft.lovelydrop.shared;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ComplexItemStack;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemFactory {

    public static <T> ComplexItemStack<T> createItem(Material material) {
        return createItem(material, null, null, null, null);
    }

    public static <T> ComplexItemStack<T> createItem(Material material, String displayName,
            List<String> lore) {
        return createItem(material, displayName, lore, null, null);
    }

    public static <T> ComplexItemStack<T> createItem(ItemStack item, Map<String, T> nbtData) {
        return createItem(item.getType(), item.getItemMeta().getDisplayName(),
                item.getItemMeta().getLore(), null, nbtData);
    }

    public static <T> ComplexItemStack<T> createItem(Material material, String displayName,
            List<String> lore, Map<Enchantment, Integer> enchantments, Map<String, T> nbtData) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);

        // Add additional enchantments.
        if (enchantments != null) {
            item.addUnsafeEnchantments(enchantments);
        }

        item.setItemMeta(itemMeta);

        // Set nbt data with direct apply.
        NBTItem nbtItem = new NBTItem(item, true);
        if (nbtData != null) {
            setNbtData(nbtItem, nbtData);
        }

        return new ComplexItemStack<>(item.clone(), nbtData);
    }

    private static <T> void setNbtData(NBTCompound nbtCompound, Map<String, T> nbtData) {
        for (Map.Entry<String, T> entry : nbtData.entrySet()) {
            String key = entry.getKey();
            T value = entry.getValue();

            if (value instanceof Map) {
                NBTCompound nestedCompound = nbtCompound.addCompound(key);
                setNbtData(nestedCompound, (Map<String, T>) value);
            } else if (value instanceof Integer) {
                nbtCompound.setInteger(key, (Integer) value);
            } else if (value instanceof Double) {
                nbtCompound.setDouble(key, (Double) value);
            } else if (value instanceof String) {
                nbtCompound.setString(key, (String) value);
            } else if (value instanceof Boolean) {
                nbtCompound.setBoolean(key, (Boolean) value);
            }
        }
    }
}
