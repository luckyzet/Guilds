package com.luckyzz.guilds.config.level;

import com.akamecoder.api.Typable;
import org.jetbrains.annotations.NotNull;

public interface ConfigLevelCondition<T> extends Typable<T> {

    int getIndex();

    @NotNull ConditionType getConditionType();

    int getNeed();

}
