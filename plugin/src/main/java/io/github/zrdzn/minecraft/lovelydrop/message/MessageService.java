/*
 * Copyright (c) 2022 zrdzn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.zrdzn.minecraft.lovelydrop.message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class MessageService {

    private final MessageCache cache;

    public MessageService(MessageCache cache) {
        this.cache = cache;
    }

    public CompletableFuture<Void> send(CommandSender receiver, String key, String... placeholders) {
        return CompletableFuture.runAsync(() -> {
            String message = this.cache.getMessage(key);

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
