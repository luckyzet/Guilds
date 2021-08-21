package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.level.ConfigLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

class GuildLevelImpl implements GuildLevel {

    private final ConfigLevel level;
    private final ExecutingConditionSet conditionList;

    GuildLevelImpl(@NotNull final ConfigLevel level, @NotNull final Set<ExecutingLevelCondition<?>> conditions) {
        this.level = level;
        this.conditionList = new ExecutingConditionSet(conditions);
    }

    @Override
    public @NotNull ConfigLevel getLevel() {
        return level;
    }

    @Override
    public @NotNull ExecutingConditionSet getConditionSet() {
        return conditionList;
    }

    @Override
    public @NotNull String toString() {
        return "GuildLevelImpl{" +
                "level=" + level +
                ", conditionList=" + conditionList +
                '}';
    }

}
