package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConfigLevelCondition;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractLevelCondition<T> implements ConfigLevelCondition<T> {

    private final int index;
    private final T type;
    private final int need;

    protected AbstractLevelCondition(final int index, @NotNull final T type, final int need) {
        this.index = index;
        this.type = type;
        this.need = need;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public @NotNull T getType() {
        return type;
    }

    @Override
    public int getNeed() {
        return need;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractLevelCondition<?> that = (AbstractLevelCondition<?>) o;
        return new EqualsBuilder()
                .append(index, that.index)
                .append(need, that.need)
                .append(type, that.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(type)
                .append(need)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "AbstractLevelCondition{" +
                "type=" + type +
                ", index=" + index +
                ", need=" + need +
                '}';
    }

}
