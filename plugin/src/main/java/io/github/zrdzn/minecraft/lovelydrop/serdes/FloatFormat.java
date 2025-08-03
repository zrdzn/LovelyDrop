package io.github.zrdzn.minecraft.lovelydrop.serdes;

/**
 * Utility class that holds float with removed trailing zeroes along with original value.
 * Transformer serializes and deserializes float from/to configuration files and formats it.
 */
public class FloatFormat {

    private final float value;
    private final String formattedValue;

    public FloatFormat(float value) {
        this(value, String.valueOf(value));
    }

    public FloatFormat(float value, String formattedValue) {
        this.value = value;
        this.formattedValue = formattedValue;
    }

    public float getValue() {
        return this.value;
    }

    public String getFormattedValue() {
        return this.formattedValue;
    }
}
