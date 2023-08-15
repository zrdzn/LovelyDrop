package io.github.zrdzn.minecraft.lovelydrop.user.infra;

import javax.sql.DataSource;
import com.google.gson.Gson;

/**
 * Sqlite implementation of {@link SqlUserSettingRepository}.
 */
public class SqliteUserSettingRepository extends SqlUserSettingRepository {

    private static final String CREATE_OR_UPDATE_USER_SETTING =
            "INSERT INTO users_settings (player_id, disabled_drops, drops_to_inventory) VALUES (?, ?, ?)" +
                    "ON CONFLICT(player_id) DO UPDATE SET disabled_drops=EXCLUDED.disabled_drops, drops_to_inventory=EXCLUDED.drops_to_inventory;";
    private static final String FIND_ELIMINATION_BY_PLAYER_ID =
            "SELECT disabled_drops, drops_to_inventory FROM users_settings WHERE player_id = ?;";

    public SqliteUserSettingRepository(DataSource dataSource, Gson gson) {
        super(dataSource, gson, CREATE_OR_UPDATE_USER_SETTING, FIND_ELIMINATION_BY_PLAYER_ID);
    }

}
