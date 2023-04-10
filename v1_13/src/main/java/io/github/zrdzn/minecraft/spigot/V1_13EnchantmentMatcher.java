package io.github.zrdzn.minecraft.spigot;

import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class V1_13EnchantmentMatcher implements EnchantmentMatcher {

    @Override
    public Optional<Enchantment> matchEnchantment(String enchantmentName) {
        return Optional.ofNullable(Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName)));
    }

}
