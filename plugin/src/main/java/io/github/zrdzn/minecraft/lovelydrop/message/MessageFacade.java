package io.github.zrdzn.minecraft.lovelydrop.message;

import io.github.zrdzn.minecraft.lovelydrop.serdes.ColoredText;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class MessageFacade {

    private final Plugin plugin;

    public MessageFacade(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(CommandSender receiver, ColoredText message, String... placeholders) {
        if (!message.getText().isEmpty()) {
            receiver.sendMessage(this.formatPlaceholders(message.getText(), placeholders));
        }
    }

    public void sendMessageAsync(CommandSender receiver, ColoredText message,
            String... placeholders) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin,
                () -> this.sendMessage(receiver, message, placeholders));
    }

    private String formatPlaceholders(String message, String... placeholders) {
        int length = placeholders.length;
        if (length == 0 || length % 2 != 0) {
            return message;
        }

        for (int index = 0; index < length; index += 2) {
            message = StringUtils.replace(message, placeholders[index], placeholders[index + 1]);
        }

        return message;
    }
}
