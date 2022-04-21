package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.MessageParser;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DropCommand implements CommandExecutor {

    private final MessageParser messageParser;
    private final MenuService menuService;

    public DropCommand(MessageParser messageParser, MenuService menuService) {
        this.messageParser = messageParser;
        this.menuService = menuService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.messageParser.getExecutedAsConsole());
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lovelydrop.menu.open")) {
            player.sendMessage(this.messageParser.getNoPermissions());
            return true;
        }

        if (!this.menuService.open(player)) {
            player.sendMessage(this.messageParser.getMenuOpenError());
            return true;
        }

        return true;
    }

}
