package io.github.zrdzn.minecraft.lovelydrop.user;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserSettingRepository {

    UserSetting createUserSetting(UUID playerId, Set<String> disabledDrops, Map<String, Boolean> dropsToInventory);

    Optional<UserSetting> findUserSettingByPlayerId(UUID playerId);

}
