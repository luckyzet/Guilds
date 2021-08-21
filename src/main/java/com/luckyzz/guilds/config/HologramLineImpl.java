package com.luckyzz.guilds.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class HologramLineImpl implements HologramLine {

    private final HologramLineType type;
    private final int index;
    private final String text;

    HologramLineImpl(final int index, @NotNull final HologramLineType type, @NotNull final String text) {
        this.index = index;
        this.type = type;
        this.text = text;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public @NotNull HologramLineType getType() {
        return type;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof HologramLineImpl)) return false;
        final HologramLineImpl that = (HologramLineImpl) o;
        return new EqualsBuilder()
                .append(index, that.index)
                .append(type, that.type)
                .append(text, that.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(type)
                .append(text)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "HologramLineImpl{" +
                "index=" + index +
                ", type=" + type +
                ", text='" + text + '\'' +
                '}';
    }

}
