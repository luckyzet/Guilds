package com.luckyzz.guilds.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class MenuDataImpl implements MenuData {

    private final MenuDataType type;
    private final String title;

    MenuDataImpl(@NotNull final MenuDataType type, @NotNull final String title) {
        this.type = type;
        this.title = title;
    }

    @Override
    public @NotNull MenuDataType getType() {
        return type;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuDataImpl)) return false;
        final MenuDataImpl that = (MenuDataImpl) o;
        return new EqualsBuilder()
                .append(type, that.type)
                .append(title, that.title)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(title)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "MenuDataImpl{" +
                "type=" + type +
                ", title='" + title + '\'' +
                '}';
    }

}
