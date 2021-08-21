package com.luckyzz.guilds.config.level;

import com.akamecoder.util.NameSelector;
import org.jetbrains.annotations.NotNull;

public enum FeatureType {

    UPGRADE_GUILD,
    TROPHIES,
    BANK,
    STORE,
    STORE_ITEM,
    EXTRA_BANK_PAGES;

    public static @NotNull FeatureType fromString(@NotNull final String string) {
        return NameSelector.select(values(), string);
    }

}
