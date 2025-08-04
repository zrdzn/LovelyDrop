package io.github.zrdzn.minecraft.lovelydrop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.serdes.okaeri.SerdesOkaeri;
import eu.okaeri.configs.serdes.okaeri.range.section.SerdesRangeSection;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import io.github.zrdzn.minecraft.lovelydrop.config.PluginConfig;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropCommand;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropListener;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuFacade;
import io.github.zrdzn.minecraft.lovelydrop.menu.MenuFactory;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ColoredTextTransformer;
import io.github.zrdzn.minecraft.lovelydrop.serdes.ComplexItemStackSerializer;
import io.github.zrdzn.minecraft.lovelydrop.serdes.FloatFormatTransformer;
import io.github.zrdzn.minecraft.lovelydrop.storage.Storage;
import io.github.zrdzn.minecraft.lovelydrop.storage.StorageException;
import io.github.zrdzn.minecraft.lovelydrop.storage.StorageFactory;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingFacade;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingFacadeFactory;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingListener;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingTask;
import io.github.zrdzn.minecraft.spigot.SpigotAdapter;
import io.github.zrdzn.minecraft.spigot.V1_12SpigotAdapter;
import io.github.zrdzn.minecraft.spigot.V1_13SpigotAdapter;
import io.github.zrdzn.minecraft.spigot.V1_8SpigotAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LovelyDropPlugin extends JavaPlugin {

    private final Logger logger = LoggerFactory.getLogger(LovelyDropPlugin.class);

    private Listener userSettingListener;
    private Listener dropListener;

    private BukkitTask userSettingBukkitTask;

    @Override
    public void onEnable() {
        this.start();
    }

    @Override
    public void onDisable() {
        this.shutdown();
    }

    public void start() {
        new Metrics(this, 19396);

        PluginConfig config;
        try {
            config = ConfigManager.create(PluginConfig.class, (it) -> {
                it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer()),
                        new SerdesBukkit(), new SerdesOkaeri(), new SerdesRangeSection());
                it.withBindFile(new File(this.getDataFolder(), "config.yml"));
                it.withSerdesPack(registry -> {
                    registry.register(new ColoredTextTransformer());
                    registry.register(new FloatFormatTransformer());
                    registry.register(new ComplexItemStackSerializer());
                });
                it.withRemoveOrphans(true);
                it.saveDefaults();
                it.load(true);
            });
        } catch (OkaeriException exception) {
            this.logger.error("Could not load the plugin configuration.", exception);
            this.shutdown();
            return;
        }

        SpigotAdapter spigotAdapter = this.prepareSpigotAdapter();
        this.logger.info("Using server engine adapter for version v{}", spigotAdapter.getVersion());

        MessageFacade messageFacade = new MessageFacade(this);

        Storage storage;
        try {
            storage = new StorageFactory(config.storage, this.getDataFolder()).createStorage();
        } catch (StorageException exception) {
            this.logger.error("Could not create the storage.", exception);
            this.shutdown();
            return;
        }

        Gson gson = new GsonBuilder().serializeNulls().create();

        UserSettingFacade userSettingFacade =
                new UserSettingFacadeFactory(storage, gson).createUserSettingFacade();
        UserSettingTask userSettingTask = new UserSettingTask(userSettingFacade);

        BukkitScheduler scheduler = this.getServer().getScheduler();
        this.userSettingBukkitTask = scheduler.runTaskTimerAsynchronously(this, userSettingTask,
                20L, config.userSettingsSaveInterval * 20L);

        this.registerListeners(config, spigotAdapter, messageFacade, userSettingFacade);

        MenuFactory menuFactory = new MenuFactory(config, messageFacade);
        MenuFacade menuFacade =
                new MenuFacade(config, menuFactory, messageFacade, userSettingFacade);

        this.getCommand("lovelydrop").setExecutor(new LovelyDropCommand(config, messageFacade));
        this.getCommand("drop").setExecutor(new DropCommand(config, messageFacade, menuFacade));
    }

    public void shutdown() {
        if (this.userSettingBukkitTask != null) {
            this.userSettingBukkitTask.cancel();
        }

        this.unregisterListeners();

        this.getServer().getPluginManager().disablePlugin(this);
    }

    public void registerListeners(PluginConfig config, SpigotAdapter spigotAdapter,
            MessageFacade messageFacade, UserSettingFacade userSettingFacade) {
        PluginManager pluginManager = this.getServer().getPluginManager();

        Map<String, Boolean> defaultDropsToInventory = new HashMap<>();
        config.drops.keySet().forEach(key -> defaultDropsToInventory.put(key, true));

        this.userSettingListener =
                new UserSettingListener(this, userSettingFacade, defaultDropsToInventory);
        pluginManager.registerEvents(this.userSettingListener, this);

        this.dropListener =
                new DropListener(config, spigotAdapter, messageFacade, userSettingFacade);
        pluginManager.registerEvents(this.dropListener, this);
    }

    public void unregisterListeners() {
        if (this.userSettingListener != null) {
            PlayerJoinEvent.getHandlerList().unregister(this.userSettingListener);
            PlayerQuitEvent.getHandlerList().unregister(this.userSettingListener);
        }

        if (this.dropListener != null) {
            BlockBreakEvent.getHandlerList().unregister(this.dropListener);
        }
    }

    public SpigotAdapter prepareSpigotAdapter() {
        // Checks if the api version is greater than 1.12.2.
        try {
            Class.forName("org.bukkit.event.block.BlockBreakEvent")
                    .getDeclaredMethod("setDropItems", boolean.class);
        } catch (Exception exception) {
            return new V1_8SpigotAdapter();
        }

        Class<?> namespaced;
        try {
            namespaced = Class.forName("org.bukkit.NamespacedKey");
        } catch (ClassNotFoundException exception) {
            return new V1_8SpigotAdapter();
        }

        try {
            Class.forName("org.bukkit.enchantments.Enchantment").getDeclaredMethod("getByKey",
                    namespaced);
        } catch (Exception exception) {
            return new V1_12SpigotAdapter();
        }

        return new V1_13SpigotAdapter();
    }
}
