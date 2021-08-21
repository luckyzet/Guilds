package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConfigLevelFeature;
import com.luckyzz.guilds.config.level.FeatureType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractLevelFeature implements ConfigLevelFeature {

    private final FeatureType type;

    AbstractLevelFeature(@NotNull final FeatureType type) {
        this.type = type;
    }

    @Override
    public @NotNull FeatureType getType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractLevelFeature that = (AbstractLevelFeature) o;
        return new EqualsBuilder()
                .append(type, that.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "AbstractLevelFeature{" +
                "type=" + type +
                '}';
    }

}
