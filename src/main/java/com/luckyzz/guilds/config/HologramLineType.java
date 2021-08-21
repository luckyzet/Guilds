package com.luckyzz.guilds.config;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum HologramLineType {

    COUNT_MEMBERS,
    GUILD_LEVEL,
    GUILD_OWNER,
    DATE,
    IDENTIFIER;

    public static @NotNull HologramLineType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
