package io.github.zrdzn.minecraft.spigot;

public class V1_8SpigotAdapter implements SpigotAdapter {

    @Override
    public String getVersion() {
        return "1.8";
    }

    @Override
    public BlockBreakHelper getBlockBreakHelper() {
        return new V1_8BlockBreakHelper();
    }
}
