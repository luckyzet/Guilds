package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConditionType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ItemLevelCondition extends AbstractLevelCondition<Material> {

    ItemLevelCondition(final int index, @NotNull final Material type, final int need) {
        super(index, type, need);
    }

    @Override
    public @NotNull
    ConditionType getConditionType() {
        return ConditionType.ITEMS;
    }

    @Override
    public @NotNull String toString() {
        return "ItemLevelCondition{} " + super.toString();
    }

}
