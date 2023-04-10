package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.menu.MenuService;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DropCommand implements CommandExecutor {

    private final MessageService messageService;
    private final MenuService menuService;

    public DropCommand(MessageService messageService, MenuService menuService) {
        this.messageService = messageService;
        this.menuService = menuService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        // Check if sender is console.
        if (!(sender instanceof Player)) {
            this.messageService.send(sender, "executed-as-console");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lovelydrop.menu.open")) {
            this.messageService.send(sender, "no-permissions");
            return true;
        }

        // Try to open the inventory.
        if (!this.menuService.open(player)) {
            this.messageService.send(sender, "menu-open-error");
            return true;
        }

        return true;
    }

}
