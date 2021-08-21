package com.luckyzz.guilds.config.level;

import com.akamecoder.api.Typable;
import com.luckyzz.guilds.config.ConfigConditionSet;
import com.luckyzz.guilds.config.ConfigFeatureSet;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ConfigLevel extends Typable<LevelType> {

    int getIndex();

    @NotNull ConfigConditionSet getConditionSet();

    @NotNull ConfigFeatureSet getFeatureSet();

    @NotNull Optional<ConfigLevel> getNextConfigLevel();

}
