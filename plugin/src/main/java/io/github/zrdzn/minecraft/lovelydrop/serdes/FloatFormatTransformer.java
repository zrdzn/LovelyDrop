package io.github.zrdzn.minecraft.lovelydrop.serdes;

import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import org.jetbrains.annotations.NotNull;

/**
 * Float transformer which removes trailing zeroes and holds it along with the original value.
 */
public class FloatFormatTransformer extends BidirectionalTransformer<Float, FloatFormat> {

    @Override
    public GenericsPair<Float, FloatFormat> getPair() {
        return this.genericsPair(Float.class, FloatFormat.class);
    }

    @Override
    public FloatFormat leftToRight(@NotNull Float data, @NotNull SerdesContext serdesContext) {
        String formattedValue = String.valueOf(data);
        formattedValue = formattedValue.contains(".") ? formattedValue.replaceAll("0*$","").replaceAll("\\.$","") : formattedValue;
        return new FloatFormat(data, formattedValue);
    }

    @Override
    public Float rightToLeft(FloatFormat data, @NotNull SerdesContext serdesContext) {
        return data.getValue();
    }

}
