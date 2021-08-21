package com.luckyzz.guilds.config;

import com.akamecoder.api.Typable;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityHead extends Typable<EntityType> {

    @NotNull EntityHeadType getHeadType();

    @Nullable String getDisplayName();

    @Nullable Material getMaterial();

    @Nullable String getSkull();

}
