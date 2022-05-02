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

import io.github.zrdzn.minecraft.lovelydrop.drop.DropCommand;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropListener;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItemCache;
import io.github.zrdzn.minecraft.lovelydrop.drop.ItemParser;
import io.github.zrdzn.minecraft.lovelydrop.menu.Menu;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuParser;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuService;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageCache;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageLoader;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import io.github.zrdzn.minecraft.lovelydrop.user.UserListener;
import io.github.zrdzn.minecraft.spigot.EnchantmentMatcher;
import io.github.zrdzn.minecraft.spigot.SpigotAdapter;
import io.github.zrdzn.minecraft.spigot.V1_12SpigotAdapter;
import io.github.zrdzn.minecraft.spigot.V1_13SpigotAdapter;
import io.github.zrdzn.minecraft.spigot.V1_8SpigotAdapter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LovelyDropPlugin extends JavaPlugin {

    private final Logger logger = this.getLogger();
    private final UserCache userCache = new UserCache();

    private DropItemCache dropItemCache;
    private Menu menu;
    private MessageService messageService;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        SpigotAdapter spigotAdapter = this.prepareSpigotAdapter();

        this.loadConfigurations(spigotAdapter);

        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new UserListener(this.userCache, this.dropItemCache), this);
        pluginManager.registerEvents(new DropListener(this.logger, this.messageService, spigotAdapter, this.dropItemCache,
            this.userCache), this);

        MenuService menuService = new MenuService(this.logger, this.messageService, this.menu, this.userCache);

        this.getCommand("lovelydrop").setExecutor(new LovelyDropCommand(this.messageService, this));
        this.getCommand("drop").setExecutor(new DropCommand(this.messageService, menuService));
    }

    @Override
    public void onDisable() {
        this.dropItemCache.getDrops().clear();
    }

    public void loadConfigurations(SpigotAdapter spigotAdapter) {
        try {
            this.reloadConfig();

            Configuration configuration = this.getConfig();

            this.dropItemCache = new DropItemCache();

            EnchantmentMatcher enchantmentMatcher = spigotAdapter.getEnchantmentMatcher();

            ItemParser itemParser = new ItemParser(this.logger, enchantmentMatcher);
            itemParser.parseMany(configuration.getConfigurationSection("drops")).forEach(this.dropItemCache::addDrop);

            MenuParser menuParser = new MenuParser(this.logger, this.dropItemCache);
            this.menu = menuParser.parse(configuration.getConfigurationSection("menu"));

            MessageCache messageCache = new MessageCache();

            MessageLoader messageLoader = new MessageLoader(messageCache);
            messageLoader.load(configuration.getConfigurationSection("messages"));

            this.messageService = new MessageService(messageCache);
        } catch (InvalidConfigurationException exception) {
            this.logger.severe("Something went wrong while parsing configuration/s.");
            exception.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    public SpigotAdapter prepareSpigotAdapter() {
        // Checks if the api version is greater than 1.12.2.
        try {
            Class.forName("org.bukkit.event.block.BlockBreakEvent").getDeclaredMethod("setDropItems", boolean.class);
        } catch (Exception exception) {
            return new V1_8SpigotAdapter();
        }

        Class<?> namespaced;
        try {
            namespaced = Class.forName("org.bukkit.NamespacedKey");
        } catch (ClassNotFoundException exception) {
            this.logger.severe("Class 'NamespacedKey' not found on 1.12.2+. Using 1.8 spigot adapter.");
            return new V1_8SpigotAdapter();
        }

        try {
            Class.forName("org.bukkit.enchantments.Enchantment").getDeclaredMethod("getByKey", namespaced);
        } catch (Exception exception) {
            return new V1_12SpigotAdapter();
        }

        return new V1_13SpigotAdapter();
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(List<String> messages) {
        return messages.stream()
            .map(LovelyDropPlugin::color)
            .collect(Collectors.toList());
    }

}
