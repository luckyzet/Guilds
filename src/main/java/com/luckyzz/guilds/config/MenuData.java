package com.luckyzz.guilds.config;

import com.akamecoder.api.Typable;
import org.jetbrains.annotations.NotNull;

public interface MenuData extends Typable<MenuDataType> {

    @NotNull String getTitle();

}
