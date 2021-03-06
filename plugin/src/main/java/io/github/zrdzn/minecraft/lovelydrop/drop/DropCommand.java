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
package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.menu.MenuService;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DropCommand implements CommandExecutor {

    private final MessageService messageService;
    private final MenuService menuService;

    public DropCommand(MessageService messageService, MenuService menuService) {
        this.messageService = messageService;
        this.menuService = menuService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        // Check if sender is console.
        if (!(sender instanceof Player)) {
            this.messageService.send(sender, "executed-as-console");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lovelydrop.menu.open")) {
            this.messageService.send(sender, "no-permissions");
            return true;
        }

        // Try to open the inventory.
        if (!this.menuService.open(player)) {
            this.messageService.send(sender, "menu-open-error");
            return true;
        }

        return true;
    }

}
