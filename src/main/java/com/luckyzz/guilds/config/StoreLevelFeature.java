package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.FeatureType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoreLevelFeature extends AmountLevelFeature {

    private final int index;
    private final CurrencyType currency;
    private final ItemStack stack;
    private final double money;

    StoreLevelFeature(final int index, @NotNull final CurrencyType currency, @NotNull final FeatureType type, @NotNull final ItemStack stack,
                      final int amount, final double money) {
        super(type, amount);
        this.index = index;
        this.currency = currency;
        this.stack = stack;
        this.money = money;
    }

    public int getIndex() {
        return index;
    }

    public @NotNull CurrencyType getCurrency() {
        return currency;
    }

    public @NotNull ItemStack getStack() {
        return stack;
    }

    public double getMoney() {
        return money;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StoreLevelFeature that = (StoreLevelFeature) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(index, that.index)
                .append(currency, that.currency)
                .append(stack, that.stack)
                .append(money, that.money)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(index)
                .append(currency)
                .append(stack)
                .append(money)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "ItemLevelFeature{" +
                "currency=" + currency +
                ", index=" + index +
                ", stack=" + stack +
                ", money=" + money +
                "} " + super.toString();
    }

}
