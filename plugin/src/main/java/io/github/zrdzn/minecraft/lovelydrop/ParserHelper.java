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

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.material.MaterialData;

import java.util.AbstractMap;
import java.util.Map.Entry;

public class ParserHelper {

    public static Entry<Integer, Integer> parseRange(String input, boolean negative)
        throws InvalidConfigurationException {
        String[] inputArray = input.split(":");
        if (inputArray.length == 0) {
            throw new InvalidConfigurationException("The value for the entry is empty.");
        } else if (inputArray.length == 1) {
            try {
                int number = negative ? Integer.parseUnsignedInt(inputArray[0]) : Integer.parseInt(inputArray[0]);

                return new AbstractMap.SimpleEntry<>(number, number);
            } catch (NumberFormatException exception) {
                throw new InvalidConfigurationException("The entry does not contain a valid number. Input: " + input);
            }
        } else {
            try {
                int minimum;
                int maximum;
                if (negative) {
                    minimum = Integer.parseInt(inputArray[0]);
                    maximum = Integer.parseInt(inputArray[1]);
                } else {
                    minimum = Integer.parseUnsignedInt(inputArray[0]);
                    maximum = Integer.parseUnsignedInt(inputArray[1]);
                }

                if (minimum > maximum) {
                    throw new InvalidConfigurationException("The first number cannot be bigger than the second one.");
                }

                return new AbstractMap.SimpleEntry<>(minimum, maximum);
            } catch (NumberFormatException exception) {
                throw new InvalidConfigurationException("The entry does not contain valid numbers. Input: " + input);
            }
        }
    }

    public static MaterialData parseLegacyMaterial(String input) throws InvalidConfigurationException {
        String[] inputArray = input.split(":");
        if (inputArray.length == 0) {
            throw new InvalidConfigurationException("The value for the entry is empty.");
        }

        String materialRaw = inputArray[0];

        Material material = Material.matchMaterial(materialRaw);
        if (material == null) {
            throw new InvalidConfigurationException("Material '" + materialRaw + "' does not exist.");
        }

        if (inputArray.length == 1) {
            return new MaterialData(material);
        }

        try {
            byte data = Byte.parseByte(inputArray[1]);

            return new MaterialData(material, data);
        } catch (NumberFormatException exception) {
            throw new InvalidConfigurationException("The input does not contain valid byte data. Input: " + input);
        }
    }

}
