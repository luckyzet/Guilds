package com.luckyzz.guilds.guild;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GuildItemImpl implements GuildItem {

    private final int index;
    private ItemStack stack;

    GuildItemImpl(final int index, @NotNull final ItemStack stack) {
        this.index = index;
        this.stack = stack;
    }

    @Override
    public int getPage() {
        return (index / 26) + 1;
    }

    @Override
    public int getSlot() {
        return index - ((getPage() - 1) * 26);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public @NotNull ItemStack getStack() {
        return stack;
    }

    @Override
    public void updateStack(@NotNull final ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildItemImpl)) return false;
        final GuildItemImpl guildItem = (GuildItemImpl) o;
        return new EqualsBuilder()
                .append(index, guildItem.index)
                .append(stack, guildItem.stack)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(stack)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "GuildItemImpl{" +
                "index=" + index +
                ", stack=" + stack +
                '}';
    }

}
