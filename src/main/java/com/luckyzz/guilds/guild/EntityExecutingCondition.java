package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.level.ConditionType;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class EntityExecutingCondition extends AbstractExecutingCondition<EntityType> {

    EntityExecutingCondition(final int index, @NotNull final EntityType type, final int need, final int amount) {
        super(index, type, need, amount);
    }

    EntityExecutingCondition(final int index, @NotNull final EntityType type, final int need) {
        super(index, type, need, 0);
    }

    @Override
    public @NotNull ConditionType getConditionType() {
        return ConditionType.MOBS;
    }

    @Override
    public @NotNull String toString() {
        return "EntityExecutingCondition{} " + super.toString();
    }

}
