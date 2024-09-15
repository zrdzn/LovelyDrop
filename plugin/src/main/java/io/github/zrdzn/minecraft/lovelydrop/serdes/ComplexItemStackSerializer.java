package io.github.zrdzn.minecraft.lovelydrop.serdes;

import java.util.Map;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import io.github.zrdzn.minecraft.lovelydrop.shared.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Complex itemstack serializer which serializes/deserializes it from/to configuration file.
 */
public class ComplexItemStackSerializer implements ObjectSerializer<ComplexItemStack<?>> {

    @Override
    public boolean supports(@NotNull Class<? super ComplexItemStack<?>> type) {
        return ComplexItemStack.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(ComplexItemStack<?> complexItemStack, SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("stack", complexItemStack.getItemStack(), ItemStack.class);
        if (complexItemStack.getNbtData() != null) {
            Map<String, Object> nbtData = (Map<String, Object>) complexItemStack.getNbtData();
            data.addAsMap("nbt", nbtData, String.class, Object.class);
        }
    }

    @Override
    public ComplexItemStack<?> deserialize(DeserializationData data, @NotNull GenericsDeclaration generics) {
        ItemStack itemStack = data.get("stack", ItemStack.class);
        Map<String, Object> nbtData = data.getAsMap("nbt", String.class, Object.class);

        return ItemFactory.createItem(itemStack, nbtData);
    }

}
