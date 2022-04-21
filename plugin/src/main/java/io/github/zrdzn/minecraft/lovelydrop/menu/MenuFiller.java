package io.github.zrdzn.minecraft.lovelydrop.menu;

import org.bukkit.Material;

public class MenuFiller {

    private final boolean enabled;
    private final Material type;
    private final String displayName;

    public MenuFiller(boolean enabled, Material type, String displayName) {
        this.enabled = enabled;
        this.type = type;
        this.displayName = displayName;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Material getType() {
        return this.type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
