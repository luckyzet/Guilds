package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.ConfigFeatureSet;
import com.luckyzz.guilds.config.level.ConfigLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface GuildLevel {

    @NotNull ConfigLevel getLevel();

    @NotNull ExecutingConditionSet getConditionSet();

    default int getIndex() {
        return getLevel().getIndex();
    }

    default @NotNull ConfigFeatureSet getFeatureSet() {
        return getLevel().getFeatureSet();
    }

    default @NotNull Optional<ConfigLevel> getNextLevel() {
        return getLevel().getNextConfigLevel();
    }

}
