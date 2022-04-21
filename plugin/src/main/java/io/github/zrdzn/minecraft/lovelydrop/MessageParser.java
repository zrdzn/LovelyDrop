package io.github.zrdzn.minecraft.lovelydrop;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class MessageParser {

    private String executedAsConsole;
    private String noPermissions;
    private String menuOpenError;
    private String pluginReloaded;
    private String notEnoughArguments;
    private String notValidArgument;

    public void parse(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("messages")) {
            throw new InvalidConfigurationException("Provided section is not 'messages'.");
        }

        this.executedAsConsole = LovelyDropPlugin.color(section.getString("executed-as-console"));
        this.noPermissions = LovelyDropPlugin.color(section.getString("no-permissions"));
        this.menuOpenError = LovelyDropPlugin.color(section.getString("menu-open-error"));
        this.pluginReloaded = LovelyDropPlugin.color(section.getString("plugin-reloaded"));
        this.notEnoughArguments = LovelyDropPlugin.color(section.getString("not-enough-arguments"));
        this.notValidArgument = LovelyDropPlugin.color(section.getString("not-valid-argument"));
    }

    public String getExecutedAsConsole() {
        return this.executedAsConsole;
    }

    public String getNoPermissions() {
        return this.noPermissions;
    }

    public String getMenuOpenError() {
        return this.menuOpenError;
    }

    public String getPluginReloaded() {
        return this.pluginReloaded;
    }

    public String getNotEnoughArguments() {
        return this.notEnoughArguments;
    }

    public String getNotValidArgument() {
        return this.notValidArgument;
    }

}
