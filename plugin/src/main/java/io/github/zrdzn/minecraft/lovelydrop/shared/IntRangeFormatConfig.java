package io.github.zrdzn.minecraft.lovelydrop.shared;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.github.zrdzn.minecraft.lovelydrop.transformer.ColoredText;

public class IntRangeFormatConfig extends OkaeriConfig {

    @Comment("Used when minimum and maximum are equal.")
    private ColoredText fixed;

    @Comment(" ")
    @Comment("Used when minimum and maximum are different.")
    private ColoredText different;

    public IntRangeFormatConfig(String fixed, String different) {
        this.fixed = new ColoredText(fixed);
        this.different = new ColoredText(different);
    }

    public ColoredText getFixed() {
        return this.fixed;
    }

    public void setFixed(ColoredText fixed) {
        this.fixed = fixed;
    }

    public ColoredText getDifferent() {
        return this.different;
    }

    public void setDifferent(ColoredText different) {
        this.different = different;
    }

}
