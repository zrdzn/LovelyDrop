package io.github.zrdzn.minecraft.lovelydrop;

import io.github.zrdzn.minecraft.lovelydrop.message.MessageConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class LovelyDropCommand implements CommandExecutor {

    private final MessageFacade messageFacade;
    private final MessageConfig messageConfig;
    private final LovelyDropPlugin plugin;

    public LovelyDropCommand(MessageFacade messageFacade, MessageConfig messageConfig, LovelyDropPlugin plugin) {
        this.messageFacade = messageFacade;
        this.messageConfig = messageConfig;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("lovelydrop.reload")) {
            this.messageFacade.sendMessageAsync(sender, this.messageConfig.getNoPermissions());
            return true;
        }

        if (args.length == 0) {
            this.messageFacade.sendMessageAsync(sender, this.messageConfig.getNotEnoughArguments());
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            this.messageFacade.sendMessageAsync(sender, this.messageConfig.getNotValidArgument());
            return true;
        }

        this.plugin.getPluginLoader().disablePlugin(this.plugin);
        this.plugin.getPluginLoader().enablePlugin(this.plugin);

        this.messageFacade.sendMessageAsync(sender, this.messageConfig.getPluginReloaded());

        return true;
    }

}
