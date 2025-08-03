package io.github.zrdzn.minecraft.spigot;

import org.bukkit.event.block.BlockBreakEvent;

public interface BlockBreakHelper {

    void disableDrop(BlockBreakEvent event);
}
