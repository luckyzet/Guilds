package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.CurrencyType;
import org.jetbrains.annotations.NotNull;

public interface GuildStoreItem extends GuildItem {

    @NotNull CurrencyType getCurrency();

    double getMoney();

}
