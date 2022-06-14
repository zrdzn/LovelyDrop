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
package io.github.zrdzn.minecraft.lovelydrop.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MessageCache {

    private final Map<String, String> messages = new HashMap<>();

    public void addMessage(String key, String content) {
        this.messages.put(key, content);
    }

    public String getMessage(String key) {
        return this.messages.get(key);
    }

    public Map<String, String> getMessages() {
        return Collections.unmodifiableMap(this.messages);
    }

}
