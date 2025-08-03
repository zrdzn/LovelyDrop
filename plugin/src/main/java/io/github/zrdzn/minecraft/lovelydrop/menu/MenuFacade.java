package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.Optional;
import dev.triumphteam.gui.guis.Gui;
import io.github.zrdzn.minecraft.lovelydrop.PluginConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSetting;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingFacade;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuFacade {

    private final Logger logger = LoggerFactory.getLogger(MenuFacade.class);

    private final PluginConfig config;
    private final MenuFactory menuFactory;
    private final MessageFacade messageFacade;
    private final UserSettingFacade userSettingFacade;

    public MenuFacade(PluginConfig config, MenuFactory menuFactory, MessageFacade messageFacade, UserSettingFacade userSettingFacade) {
        this.config = config;
        this.menuFactory = menuFactory;
        this.messageFacade = messageFacade;
        this.userSettingFacade = userSettingFacade;
    }

    /**
     * Open the menu for the specified player.
     *
     * @param player a player to open the menu for
     */
    public void open(Player player) {
        MessageConfig messageConfig = this.config.messages;

        // Get user settings from the cache.
        Optional<UserSetting> userSettingMaybe = this.userSettingFacade.findUserSettingByPlayerIdFromCache(player.getUniqueId());
        if (!userSettingMaybe.isPresent()) {
            this.logger.error("User settings not found for {}.", player.getName());
            this.messageFacade.sendMessageAsync(player, messageConfig.needToJoinAgain);
            return;
        }

        Gui menu = this.menuFactory.createDropMenu(player, userSettingMaybe.get());

        menu.open(player);
    }

}
