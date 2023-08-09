package io.github.zrdzn.minecraft.lovelydrop.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserSettingTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(UserSettingTask.class);

    private final UserSettingFacade userSettingFacade;

    public UserSettingTask(UserSettingFacade userSettingFacade) {
        this.userSettingFacade = userSettingFacade;
    }

    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            this.userSettingFacade.saveOrUpdateAllUserSettingsToStorage();
            long end = System.currentTimeMillis();
            long difference = end - start;

            this.logger.info("All user drop settings have been saved to the storage. (Took {}ms)", difference);
        } catch (UserSettingException exception) {
            this.logger.error("Could not save user drop settings to storage.", exception);
        }
    }

}
