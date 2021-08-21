package com.luckyzz.guilds.config;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum ItemDataType {

    PANE,
    NEXT_PAGE,
    PREV_PAGE,
    STORE_ITEM,
    CLOSE,
    UNAVAILABLE,
    UPGRADE_GUILD,
    BANK,
    TROPHIES,
    STORE,
    UPGRADE_BARRIER,
    UPGRADE_DONE,
    UPGRADE_NOT_DONE,
    UPGRADE_ENTITY,
    UPGRADE_ITEM;

    public static @NotNull ItemDataType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
