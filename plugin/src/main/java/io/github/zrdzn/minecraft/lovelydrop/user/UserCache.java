package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserCache {

    private final Map<UUID, User> users = new HashMap<>();

    public void addUser(UUID id, User user) {
        this.users.put(id, user);
    }

    public void removeUser(UUID id) {
        this.users.remove(id);
    }

    public Optional<User> getUser(UUID id) {
        return Optional.ofNullable(this.users.get(id));
    }

    public Map<UUID, User> getUsers() {
        return this.users;
    }

}
