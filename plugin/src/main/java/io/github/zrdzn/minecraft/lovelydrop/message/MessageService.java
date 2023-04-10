package io.github.zrdzn.minecraft.lovelydrop.message;

import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class MessageService {

    private final MessageCache cache;

    public MessageService(MessageCache cache) {
        this.cache = cache;
    }

    public CompletableFuture<Void> send(CommandSender receiver, String key, String... placeholders) {
        return CompletableFuture.runAsync(() -> {
            String message = this.cache.getMessage(key);
            if (message == null) {
                return;
            }

            int length = placeholders.length;
            if (length <= 0 || length % 2 != 0) {
                receiver.sendMessage(message);
                return;
            }

            for (int index = 0; index < length; index += 2) {
                message = StringUtils.replace(message, placeholders[index], placeholders[index + 1]);
            }

            receiver.sendMessage(message);
        });
    }

}
