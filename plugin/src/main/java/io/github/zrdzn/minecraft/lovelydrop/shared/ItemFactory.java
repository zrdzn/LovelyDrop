package io.github.zrdzn.minecraft.lovelydrop.shared;

import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemFactory {

    public static ItemStack createItem(Material material, String displayName, List<String> lore) {
        return createItem(material, displayName, lore, null);
    }

    public static ItemStack createItem(Material material, String displayName, List<String> lore, Map<Enchantment, Integer> enchantments) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);

        if (enchantments != null) {
            item.addUnsafeEnchantments(enchantments);
        }

        item.setItemMeta(itemMeta);

        return item.clone();
    }

}
