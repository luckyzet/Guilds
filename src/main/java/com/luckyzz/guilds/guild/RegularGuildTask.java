package com.luckyzz.guilds.guild;

import com.akamecoder.task.RegularTask;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

final class RegularGuildTask extends RegularTask {

    private final GuildManager guildManager;

    RegularGuildTask(@NotNull final Plugin plugin, @NotNull final GuildManager guildManager) {
        super(plugin);
        this.guildManager = guildManager;
        this.runTaskTimerAsynchronously(20);
    }

    @Override
    public void run() {
        guildManager.getGuildSet().forEach(guild -> {
            final GuildMenuSet menuSet = guild.getMenuSet();
            menuSet.getBankMenuList().forEach(bankMenu -> {
                final Inventory inventory = bankMenu.getHolder().getInventory();
                if (inventory == null) {
                    throw new IllegalArgumentException("This inventory is null!");
                }
                final Set<GuildItem> items = new HashSet<>();
                for (int x = 0; x < 27; x++) {
                    if (GuildStorageMenu.isPaneNextPrev(x)) {
                        continue;
                    }
                    final ItemStack stack = inventory.getItem(x);
                    if (stack == null || stack.getType() == Material.AIR) {
                        continue;
                    }
                    final int index = x + ((bankMenu.getPage() - 1) * 26);
                    items.add(new GuildItemImpl(index, stack));
                }
                guildManager.updateBankPage(guild, bankMenu, items);
            });

            menuSet.getTrophiesMenuList().forEach(trophiesMenu -> {
                final Inventory inventory = trophiesMenu.getHolder().getInventory();
                if (inventory == null) {
                    throw new IllegalArgumentException("This inventory is null!");
                }
                final Set<GuildItem> items = new HashSet<>();
                for (int x = 0; x < 27; x++) {
                    if (GuildStorageMenu.isPaneNextPrev(x)) {
                        continue;
                    }
                    final ItemStack stack = inventory.getItem(x);
                    if (stack == null || stack.getType() == Material.AIR) {
                        continue;
                    }
                    final int index = x + ((trophiesMenu.getPage() - 1) * 26);
                    items.add(new GuildItemImpl(index, stack));
                }
                guildManager.updateTrophiesPage(guild, trophiesMenu, items);
            });
        });
    }

}
