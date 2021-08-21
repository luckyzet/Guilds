package com.luckyzz.guilds.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class EntityHeadImpl implements EntityHead {

    private final EntityType type;
    private final String displayName;
    private final Material material;
    private final String skull;

    EntityHeadImpl(@NotNull final EntityType type, @Nullable final String displayName, @Nullable final Material material,
                   @Nullable final String skull) {
        this.type = type;
        this.displayName = displayName;
        this.material = material;
        this.skull = skull;
    }

    @Override
    public @NotNull EntityHeadType getHeadType() {
        return material == null ? EntityHeadType.SKULL : EntityHeadType.MATERIAL;
    }

    @Override
    public @NotNull EntityType getType() {
        return type;
    }

    @Override
    public @Nullable String getDisplayName() {
        return displayName;
    }

    @Override
    public @Nullable Material getMaterial() {
        return material;
    }

    @Override
    public @Nullable String getSkull() {
        return skull;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityHeadImpl)) return false;
        final EntityHeadImpl that = (EntityHeadImpl) o;
        return new EqualsBuilder()
                .append(type, that.type)
                .append(displayName, that.displayName)
                .append(material, that.material)
                .append(skull, that.skull)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(displayName)
                .append(material)
                .append(skull)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "EntityHeadImpl{" +
                "type=" + type +
                ", displayName=" + displayName +
                ", material=" + material +
                ", skull='" + skull + '\'' +
                '}';
    }

}
