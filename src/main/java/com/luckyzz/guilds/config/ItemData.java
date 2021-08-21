package com.luckyzz.guilds.config;

import com.akamecoder.api.Typable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface ItemData extends UnaryOperator<ItemStack>, Supplier<ItemStack>, Typable<ItemDataType> {

    @Nullable Material getMaterial();

    @Nullable String getDisplayName();

    @Nullable List<String> getLore();

}
