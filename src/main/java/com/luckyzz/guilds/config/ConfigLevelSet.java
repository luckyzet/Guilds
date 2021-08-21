package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConfigLevel;
import com.luckyzz.guilds.config.level.LevelType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class ConfigLevelSet {

    private final Set<ConfigLevel> levels;

    ConfigLevelSet(@NotNull final Set<ConfigLevel> levels) {
        this.levels = levels;
    }

    public @NotNull Stream<ConfigLevel> stream() {
        return levels.stream();
    }

    public @NotNull Optional<ConfigLevel> getDefaultLevel() {
        return stream().filter(level -> level.getType() == LevelType.FIRST).findFirst();
    }

    public @NotNull Optional<ConfigLevel> getLevel(final int index) {
        return stream().filter(level -> level.getIndex() == index).findFirst();
    }

    @Override
    public @NotNull String toString() {
        return "ConfigLevelSet{" +
                "levels=" + levels +
                '}';
    }

}
