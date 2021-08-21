package com.luckyzz.guilds.config.level;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum LevelType {

    FIRST,
    MIDDLE,
    LAST;

    public static @NotNull LevelType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
