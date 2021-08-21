package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.level.ConfigLevelCondition;

public interface ExecutingLevelCondition<T> extends ConfigLevelCondition<T> {

    boolean isExecuted();

    int getAmount();

    void setAmount(final int amount);

}
