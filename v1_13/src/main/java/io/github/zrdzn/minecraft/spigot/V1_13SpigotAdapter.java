package io.github.zrdzn.minecraft.spigot;

public class V1_13SpigotAdapter implements SpigotAdapter {

    @Override
    public BlockBreakHelper getBlockBreakHelper() {
        return new V1_12BlockBreakHelper();
    }

}
