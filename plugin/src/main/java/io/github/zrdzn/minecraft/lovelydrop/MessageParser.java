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
