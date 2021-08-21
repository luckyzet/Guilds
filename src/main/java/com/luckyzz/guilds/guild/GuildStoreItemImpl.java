package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.CurrencyType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GuildStoreItemImpl extends GuildItemImpl implements GuildStoreItem {

    private final CurrencyType currency;
    private final double money;

    GuildStoreItemImpl(@NotNull final CurrencyType currency, final int index, @NotNull final ItemStack stack, final double money) {
        super(index, stack);
        this.currency = currency;
        this.money = money;
    }

    @Override
    public @NotNull CurrencyType getCurrency() {
        return currency;
    }

    @Override
    public void updateStack(@NotNull final ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildStoreItemImpl)) return false;
        final GuildStoreItemImpl that = (GuildStoreItemImpl) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(currency, that.currency)
                .append(money, that.money)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(currency)
                .append(money)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "GuildStoreItemImpl{" +
                "money=" + money +
                ", currency=" + currency +
                "} " + super.toString();
    }

}
