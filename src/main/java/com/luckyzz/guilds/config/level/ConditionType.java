package com.luckyzz.guilds.config.level;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum ConditionType {

    MOBS,
    ITEMS;

    public static @NotNull ConditionType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
