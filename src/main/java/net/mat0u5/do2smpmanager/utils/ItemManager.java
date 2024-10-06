package net.mat0u5.do2smpmanager.utils;

import com.mojang.serialization.Codec;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ItemManager {
    public static void setComponentInt(ItemStack itemStack, String componentKey, int value) {
        if (itemStack == null) return;
        NbtCompound nbtComp = new NbtCompound();
        nbtComp.putInt(componentKey,value);
        NbtComponent.set(DataComponentTypes.CUSTOM_DATA, itemStack, nbtComp);

        //itemStack.set(DataComponentTypes.MAX_STACK_SIZE, value);
        //itemStack.apply(DataComponentTypes.LORE, LoreComponent.DEFAULT, Text.of(""), LoreComponent::with);
        //itemStack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, nbtComp);
    }

}
