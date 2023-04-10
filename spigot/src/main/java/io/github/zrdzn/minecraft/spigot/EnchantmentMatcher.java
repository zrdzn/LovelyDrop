package io.github.zrdzn.minecraft.spigot;

import java.util.Optional;
import org.bukkit.enchantments.Enchantment;

public interface EnchantmentMatcher {

    Optional<Enchantment> matchEnchantment(String enchantmentName);

}
