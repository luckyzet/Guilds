package com.luckyzz.guilds.config;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum CurrencyType {

    DEFAULT,
    COINS;

    public static @NotNull CurrencyType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
