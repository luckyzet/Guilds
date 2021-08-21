package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConfigLevelCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public class ConfigConditionSet {

    private final Set<ConfigLevelCondition<?>> conditions;

    ConfigConditionSet(@NotNull final Set<ConfigLevelCondition<?>> conditions) {
        this.conditions = conditions;
    }

    public @NotNull Stream<ConfigLevelCondition<?>> stream() {
        return conditions.stream();
    }

    public @NotNull Set<ConfigLevelCondition<?>> conditions() {
        return conditions;
    }

    @Override
    public @NotNull String toString() {
        return "ConfigConditionSet{" +
                "conditions=" + conditions +
                '}';
    }

}
