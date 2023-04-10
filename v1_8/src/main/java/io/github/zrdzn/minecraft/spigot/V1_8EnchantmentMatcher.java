package io.github.zrdzn.minecraft.spigot;

import java.util.Optional;
import org.bukkit.enchantments.Enchantment;

public class V1_8EnchantmentMatcher implements EnchantmentMatcher {

    @Override
    public Optional<Enchantment> matchEnchantment(String enchantmentName) {
        return Optional.ofNullable(Enchantment.getByName(enchantmentName.toUpperCase()));
    }

}
