package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.FeatureType;
import org.jetbrains.annotations.NotNull;

class DefaultLevelFuture extends AbstractLevelFeature {

    DefaultLevelFuture(@NotNull final FeatureType type) {
        super(type);
    }

    @Override
    public @NotNull String toString() {
        return "DefaultLevelFuture{} " + super.toString();
    }

}
