package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.level.ConditionType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ItemExecutingCondition extends AbstractExecutingCondition<Material> {

    ItemExecutingCondition(final int index, @NotNull final Material type, final int need, final int amount) {
        super(index, type, need, amount);
    }

    ItemExecutingCondition(final int index, @NotNull final Material type, final int need) {
        super(index, type, need, 0);
    }

    @Override
    public @NotNull
    ConditionType getConditionType() {
        return ConditionType.ITEMS;
    }

    @Override
    public @NotNull String toString() {
        return "ItemExecutingCondition{} " + super.toString();
    }

}
