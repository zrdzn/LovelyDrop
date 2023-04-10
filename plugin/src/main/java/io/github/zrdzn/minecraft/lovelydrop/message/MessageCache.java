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
