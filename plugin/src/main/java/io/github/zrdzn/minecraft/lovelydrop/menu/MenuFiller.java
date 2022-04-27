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
package io.github.zrdzn.minecraft.lovelydrop.menu;

import org.bukkit.Material;

public class MenuFiller {

    private final Material type;
    private final String displayName;

    public MenuFiller(Material type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public Material getType() {
        return this.type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
