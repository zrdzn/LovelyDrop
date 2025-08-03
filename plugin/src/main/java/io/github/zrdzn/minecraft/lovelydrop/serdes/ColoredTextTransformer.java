package io.github.zrdzn.minecraft.lovelydrop.serdes;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * Text transformer where left is the raw text with unparsed color codes and right is the colored
 * text.
 */
public class ColoredTextTransformer extends BidirectionalTransformer<String, ColoredText> {

    @Override
    public GenericsPair<String, ColoredText> getPair() {
        return this.genericsPair(String.class, ColoredText.class);
    }

    @Override
    public ColoredText leftToRight(@NotNull String text, @NotNull SerdesContext serdesContext) {
        return new ColoredText(ChatColor.translateAlternateColorCodes('&', text));
    }

    @Override
    public String rightToLeft(ColoredText text, @NotNull SerdesContext serdesContext) {
        return text.getText().replace(ChatColor.COLOR_CHAR, '&');
    }
}
