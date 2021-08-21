package com.luckyzz.guilds.config;

import com.akamecoder.api.Typable;
import org.jetbrains.annotations.NotNull;

public interface HologramLine extends Typable<HologramLineType> {

    int getIndex();

    @NotNull String getText();

}
