package io.github.zrdzn.minecraft.spigot;

import org.bukkit.event.block.BlockBreakEvent;

public class V1_12BlockBreakHelper implements BlockBreakHelper {

    @Override
    public void disableDrop(BlockBreakEvent event) {
        event.setDropItems(false);
    }

}
