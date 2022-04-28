/*
 * Copyright (c) 2022-2022 zrdzn
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

import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public class MessageLoader {

    public static final String DEFAULT_MESSAGE = "<message not found>";

    private final MessageCache cache;

    public MessageLoader(MessageCache cache) {
        this.cache = cache;
    }

    public void load(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("messages")) {
            throw new InvalidConfigurationException("Provided section is not 'messages'.");
        }

        section.getKeys(false).forEach(key -> this.cache.addMessage(key, LovelyDropPlugin.color(section.getString(key))));
    }

}
