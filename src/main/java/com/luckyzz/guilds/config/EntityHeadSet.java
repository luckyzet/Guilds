package com.luckyzz.guilds.config;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EntityHeadSet {

    private final Set<EntityHead> heads;

    EntityHeadSet(@NotNull final Set<EntityHead> heads) {
        this.heads = heads;
    }

    public @NotNull EntityHead getHead(@NotNull final EntityType type) {
        return heads.stream().filter(head -> head.getType() == type).findFirst()
                .orElse(new EntityHeadImpl(type, null, Material.PLAYER_HEAD, null));
    }

    @Override
    public @NotNull String toString() {
        return "EntityHeadSet{" +
                "heads=" + heads +
                '}';
    }

}
