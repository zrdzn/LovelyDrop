package io.github.zrdzn.minecraft.lovelydrop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LovelyDropCommand implements CommandExecutor {

    private final MessageParser messageParser;
    private final LovelyDropPlugin plugin;

    public LovelyDropCommand(MessageParser messageParser, LovelyDropPlugin plugin) {
        this.messageParser = messageParser;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        if (!sender.hasPermission("lovelydrop.reload")) {
            sender.sendMessage(this.messageParser.getNoPermissions());
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(this.messageParser.getNotEnoughArguments());
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(this.messageParser.getNotValidArgument());
            return true;
        }

        this.plugin.getPluginLoader().disablePlugin(this.plugin);
        this.plugin.getPluginLoader().enablePlugin(this.plugin);

        sender.sendMessage(this.messageParser.getPluginReloaded());

        return true;
    }

}
