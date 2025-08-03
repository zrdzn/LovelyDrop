package io.github.zrdzn.minecraft.spigot;

public class V1_12SpigotAdapter implements SpigotAdapter {

    @Override
    public String getVersion() {
        return "1.12";
    }

    @Override
    public BlockBreakHelper getBlockBreakHelper() {
        return new V1_12BlockBreakHelper();
    }
}
