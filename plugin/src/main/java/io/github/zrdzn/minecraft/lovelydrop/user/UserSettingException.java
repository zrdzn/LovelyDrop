package io.github.zrdzn.minecraft.lovelydrop.user;

public class UserSettingException extends RuntimeException {

    public UserSettingException(String message) {
        super(message);
    }

    public UserSettingException(String message, Throwable cause) {
        super(message, cause);
    }
}
