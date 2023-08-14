package io.github.zrdzn.minecraft.lovelydrop.serdes;

/**
 * Utility class that holds colored text by its transformer.
 * Transformer serializes and deserializes colored text from/to configuration files.
 */
public class ColoredText {

    private final String text;

    public ColoredText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public int hashCode() {
        return this.text.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ColoredText && this.text.equals(((ColoredText) object).text);
    }

    @Override
    public String toString() {
        return this.text;
    }

}
