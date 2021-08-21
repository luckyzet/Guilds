package com.luckyzz.guilds.config;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum MenuDataType {

    MAIN,
    BANK,
    UPGRADE,
    TROPHIES,
    STORE;

    public static @NotNull MenuDataType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
