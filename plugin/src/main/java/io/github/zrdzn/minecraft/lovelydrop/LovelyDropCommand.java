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
package io.github.zrdzn.minecraft.lovelydrop;

import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class LovelyDropCommand implements CommandExecutor {

    private final MessageService messageService;
    private final LovelyDropPlugin plugin;

    public LovelyDropCommand(MessageService messageService, LovelyDropPlugin plugin) {
        this.messageService = messageService;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        if (!sender.hasPermission("lovelydrop.reload")) {
            this.messageService.send(sender, "no-permissions");
            return true;
        }

        if (args.length == 0) {
            this.messageService.send(sender, "not-enough-arguments");
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            this.messageService.send(sender, "not-valid-argument");
            return true;
        }

        this.plugin.getPluginLoader().disablePlugin(this.plugin);
        this.plugin.getPluginLoader().enablePlugin(this.plugin);

        this.messageService.send(sender, "plugin-reloaded");

        return true;
    }

}
