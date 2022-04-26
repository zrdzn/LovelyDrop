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

}
