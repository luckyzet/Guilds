package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConfigLevel;
import com.luckyzz.guilds.config.level.ConfigLevelCondition;
import com.luckyzz.guilds.config.level.ConfigLevelFeature;
import com.luckyzz.guilds.config.level.LevelType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

class ConfigLevelImpl implements ConfigLevel {

    private final int index;
    private final LevelType type;
    private final ConfigConditionSet conditionSet;
    private final ConfigFeatureSet featureSet;
    private ConfigLevel level;

    ConfigLevelImpl(final int index, @NotNull final LevelType type, @NotNull final Set<ConfigLevelCondition<?>> conditionSet,
                    @NotNull final Set<ConfigLevelFeature> featureSet) {
        this.index = index;
        this.type = type;
        this.conditionSet = new ConfigConditionSet(conditionSet);
        this.featureSet = new ConfigFeatureSet(featureSet);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public @NotNull LevelType getType() {
        return type;
    }

    @Override
    public @NotNull ConfigConditionSet getConditionSet() {
        return conditionSet;
    }

    @Override
    public @NotNull ConfigFeatureSet getFeatureSet() {
        return featureSet;
    }

    @Override
    public @NotNull Optional<ConfigLevel> getNextConfigLevel() {
        return Optional.ofNullable(level);
    }

    void nextConfigLevel(@NotNull final ConfigLevel level) {
        this.level = level;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ConfigLevelImpl that = (ConfigLevelImpl) o;
        return new EqualsBuilder()
                .append(index, that.index)
                .append(conditionSet, that.conditionSet)
                .append(featureSet, that.featureSet)
                .append(level, that.level)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(conditionSet)
                .append(featureSet)
                .append(level)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "ConfigLevelImpl{" +
                "index=" + index +
                ", conditionSet=" + conditionSet +
                ", featureSet=" + featureSet +
                ", level=" + level +
                '}';
    }

}
