package io.github.zrdzn.minecraft.spigot;

public class V1_8SpigotAdapter implements SpigotAdapter {

    @Override
    public EnchantmentMatcher getEnchantmentMatcher() {
        return new V1_8EnchantmentMatcher();
    }

    @Override
    public BlockBreakHelper getBlockBreakHelper() {
        return new V1_8BlockBreakHelper();
    }

}
