package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConditionType;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class EntityLevelCondition extends AbstractLevelCondition<EntityType> {

    EntityLevelCondition(final int index, @NotNull final EntityType type, final int need) {
        super(index, type, need);
    }

    @Override
    public @NotNull
    ConditionType getConditionType() {
        return ConditionType.MOBS;
    }

    @Override
    public @NotNull String toString() {
        return "EntityLevelCondition{} " + super.toString();
    }

}
