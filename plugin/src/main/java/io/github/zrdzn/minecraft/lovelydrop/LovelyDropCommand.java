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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class LovelyDropCommand implements CommandExecutor {

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
