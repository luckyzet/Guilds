package com.luckyzz.guilds.guild;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GuildItem {

    int getPage();

    int getSlot();

    int getIndex();

    @NotNull ItemStack getStack();

    void updateStack(@NotNull final ItemStack stack);

}
