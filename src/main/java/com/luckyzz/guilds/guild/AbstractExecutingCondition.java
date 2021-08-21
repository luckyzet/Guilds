package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.AbstractLevelCondition;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractExecutingCondition<T> extends AbstractLevelCondition<T> implements ExecutingLevelCondition<T> {

    private int amount;

    AbstractExecutingCondition(final int index, @NotNull final T type, final int need, final int amount) {
        super(index, type, need);
        this.amount = amount;
    }

    @Override
    public boolean isExecuted() {
        return amount >= getNeed();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(final int amount) {
        this.amount = amount;
        isExecuted();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractExecutingCondition<?> that = (AbstractExecutingCondition<?>) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(amount, that.amount)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(amount)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "AbstractExecutingCondition{" +
                "amount=" + amount +
                "} " + super.toString();
    }

}
