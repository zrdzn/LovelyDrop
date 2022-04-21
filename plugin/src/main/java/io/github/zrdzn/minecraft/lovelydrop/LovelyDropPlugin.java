package io.github.zrdzn.minecraft.lovelydrop;

import io.github.zrdzn.minecraft.lovelydrop.drop.DropCommand;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropListener;
import io.github.zrdzn.minecraft.lovelydrop.item.ItemCache;
import io.github.zrdzn.minecraft.lovelydrop.item.ItemParser;
import io.github.zrdzn.minecraft.lovelydrop.menu.Menu;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuParser;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuService;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import io.github.zrdzn.minecraft.lovelydrop.user.UserListener;
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

    private ItemCache itemCache;
    private Menu menu;
    private MessageParser messageParser;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.loadConfigurations();

        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new UserListener(this.userCache), this);
        pluginManager.registerEvents(new DropListener(this.logger, this.itemCache, this.userCache), this);

        MenuService menuService = new MenuService(this.logger, this.menu, this.userCache);

        this.getCommand("lovelydrop").setExecutor(new LovelyDropCommand(this.messageParser, this));
        this.getCommand("drop").setExecutor(new DropCommand(this.messageParser, menuService));
    }

    @Override
    public void onDisable() {
        this.itemCache.getDrops().clear();
    }

    public void loadConfigurations() {
        try {
            this.reloadConfig();

            Configuration configuration = this.getConfig();

            this.itemCache = new ItemCache();

            ItemParser itemParser = new ItemParser(this.logger);
            itemParser.parseMany(configuration.getConfigurationSection("drops")).forEach(this.itemCache::addDrop);

            MenuParser menuParser = new MenuParser(this.logger, this.itemCache);
            this.menu = menuParser.parse(configuration.getConfigurationSection("menu"));

            this.messageParser = new MessageParser();
            this.messageParser.parse(configuration.getConfigurationSection("messages"));
        } catch (InvalidConfigurationException exception) {
            this.logger.severe("Something went wrong while parsing configuration/s.");
            exception.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }
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
