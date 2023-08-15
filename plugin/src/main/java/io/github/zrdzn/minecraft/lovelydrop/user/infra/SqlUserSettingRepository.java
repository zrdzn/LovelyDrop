package io.github.zrdzn.minecraft.lovelydrop.user.infra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.sql.DataSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSetting;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingException;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingRepository;

class SqlUserSettingRepository implements UserSettingRepository {

    private final DataSource dataSource;
    private final Gson gson;

    private final String createOrUpdateUserSettingQuery;
    private final String findUserSettingByPlayerId;

    public SqlUserSettingRepository(DataSource dataSource, Gson gson, String createOrUpdateUserSettingQuery,
                                    String findUserSettingByPlayerId) {
        this.dataSource = dataSource;
        this.gson = gson;

        this.createOrUpdateUserSettingQuery = createOrUpdateUserSettingQuery;
        this.findUserSettingByPlayerId = findUserSettingByPlayerId;
    }

    @Override
    public void createOrUpdateUserSettings(List<UserSetting> userSettings) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement createStatement = connection.prepareStatement(this.createOrUpdateUserSettingQuery)) {
            for (UserSetting userSetting : userSettings) {
                createStatement.setString(1, userSetting.getPlayerId().toString());
                createStatement.setString(2, this.gson.toJson(userSetting.getDisabledDrops()));
                createStatement.setString(3, this.gson.toJson(userSetting.getDropsToInventory()));
                createStatement.addBatch();
            }

            createStatement.executeBatch();
        } catch (SQLException exception) {
            throw new UserSettingException("Could not create user settings in database.", exception);
        }
    }

    @Override
    public void createOrUpdateUserSetting(UserSetting userSetting) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement createStatement = connection.prepareStatement(this.createOrUpdateUserSettingQuery)) {
            createStatement.setString(1, userSetting.getPlayerId().toString());
            createStatement.setString(2, this.gson.toJson(userSetting.getDisabledDrops()));
            createStatement.setString(3, this.gson.toJson(userSetting.getDropsToInventory()));

            createStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new UserSettingException("Could not create user setting in database.", exception);
        }
    }

    @Override
    public Optional<UserSetting> findUserSettingByPlayerId(UUID playerId) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(this.findUserSettingByPlayerId)) {
            statement.setString(1, playerId.toString());
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return Optional.empty();
            }

            String disabledDropsJson = result.getString("disabled_drops");
            Set<String> disabledDrops = this.gson.fromJson(disabledDropsJson, new TypeToken<HashSet<String>>(){}.getType());

            String dropsToInventoryJson = result.getString("drops_to_inventory");
            Map<String, Boolean> dropsToInventory = this.gson.fromJson(dropsToInventoryJson, new TypeToken<HashMap<String, Boolean>>(){}.getType());

            return Optional.of(
                    new UserSetting(
                            playerId,
                            disabledDrops,
                            dropsToInventory
                    )
            );
        } catch (SQLException exception) {
            throw new UserSettingException("Could not find user setting by player id.", exception);
        }
    }

}
