package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.FeatureType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AmountLevelFeature extends AbstractLevelFeature {

    private final int amount;

    AmountLevelFeature(@NotNull final FeatureType type, final int amount) {
        super(type);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AmountLevelFeature that = (AmountLevelFeature) o;
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
        return "AmountLevelFeature{" +
                "amount=" + amount +
                "} " + super.toString();
    }

}
