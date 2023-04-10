package io.github.zrdzn.minecraft.spigot;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

public class V1_8BlockBreakHelper implements BlockBreakHelper {

    @Override
    public void disableDrop(BlockBreakEvent event) {
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
    }

}
