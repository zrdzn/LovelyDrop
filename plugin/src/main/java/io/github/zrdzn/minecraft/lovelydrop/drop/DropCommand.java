package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.menu.MenuFacade;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DropCommand implements CommandExecutor {

    private final MessageFacade messageFacade;
    private final MessageConfig messageConfig;
    private final MenuFacade menuFacade;

    public DropCommand(MessageFacade messageFacade, MessageConfig messageConfig, MenuFacade menuFacade) {
        this.messageConfig = messageConfig;
        this.messageFacade = messageFacade;
        this.menuFacade = menuFacade;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Check if sender is console.
        if (!(sender instanceof Player)) {
            this.messageFacade.sendMessageAsync(sender, this.messageConfig.getExecutedAsConsole());
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lovelydrop.menu.open")) {
            this.messageFacade.sendMessageAsync(sender, this.messageConfig.getNoPermissions());
            return true;
        }

        // Open inventory for player.
        this.menuFacade.open(player);

        return true;
    }

}
