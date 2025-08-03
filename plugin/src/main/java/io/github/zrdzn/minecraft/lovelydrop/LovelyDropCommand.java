package io.github.zrdzn.minecraft.lovelydrop;

import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class LovelyDropCommand implements CommandExecutor {

    private final PluginConfig config;
    private final MessageFacade messageFacade;

    public LovelyDropCommand(PluginConfig config, MessageFacade messageFacade) {
        this.config = config;
        this.messageFacade = messageFacade;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("lovelydrop.reload")) {
            this.messageFacade.sendMessageAsync(sender, this.config.messages.noPermissions);
            return true;
        }

        if (args.length == 0) {
            this.messageFacade.sendMessageAsync(sender, this.config.messages.notEnoughArguments);
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            this.messageFacade.sendMessageAsync(sender, this.config.messages.notValidArgument);
            return true;
        }

        this.config.load();

        this.messageFacade.sendMessageAsync(sender, this.config.messages.pluginReloaded);

        return true;
    }

}
