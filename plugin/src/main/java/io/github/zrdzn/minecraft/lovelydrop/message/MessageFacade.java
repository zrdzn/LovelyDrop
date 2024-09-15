package io.github.zrdzn.minecraft.lovelydrop.message;

import io.github.zrdzn.minecraft.lovelydrop.serdes.ColoredText;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * This class is responsible for handling messages.
 */
public class MessageFacade {

    private final Plugin plugin;

    public MessageFacade(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Send message to receiver with optional placeholder replacements.
     * Replacements are in format: placeholder1, replacement1, placeholder2, replacement2, etc.
     *
     * @param receiver a receiver of the message
     * @param message a message to send
     * @param placeholders optional placeholders to replace
     */
    public void sendMessage(CommandSender receiver, ColoredText message, String... placeholders) {
        if (!message.getText().isEmpty()) {
            receiver.sendMessage(this.formatPlaceholders(message.getText(), placeholders));
        }
    }

    /**
     * Send message asynchronously to receiver with optional placeholder replacements.
     * Replacements are in format: placeholder1, replacement1, placeholder2, replacement2, etc.
     *
     * @param receiver a receiver of the message
     * @param message a message to send
     * @param placeholders optional placeholders to replace
     */
    public void sendMessageAsync(CommandSender receiver, ColoredText message, String... placeholders) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.sendMessage(receiver, message, placeholders));
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
