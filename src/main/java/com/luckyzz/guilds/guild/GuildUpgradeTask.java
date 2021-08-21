package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.ItemDataSet;
import com.luckyzz.guilds.config.ItemDataType;
import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.config.level.ConditionType;
import com.luckyzz.guilds.config.level.LevelType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.itemstack.ItemUtils;
import com.akamecoder.menu.MenuHolder;
import com.akamecoder.task.RegularTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

final class GuildUpgradeTask extends RegularTask {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;
    private boolean executed = false;

    GuildUpgradeTask(@NotNull final Plugin plugin, @NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig,
                     @NotNull final GuildManager guildManager) {
        super(plugin);
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
        this.runTaskTimerAsynchronously(0.5);
    }

    @Override
    public void run() {
        guildManager.getGuildSet().forEach(guild -> {
            final GuildMenuSet menuSet = guild.getMenuSet();

            final GuildUpgradeMenu upgradeMenu = menuSet.getUpgradeMenu();
            if (upgradeMenu != null) {
                final MenuHolder holder = upgradeMenu.getHolder();
                final Inventory inventory = holder.getInventory();
                if (inventory == null) {
                    throw new IllegalArgumentException("Menu of the upgrade menu is null now!");
                }

                final GuildLevel level = guild.getLevel();
                final ExecutingConditionSet conditionSet = level.getConditionSet();

                final ItemDataSet itemDataSet = optionConfig.getItemDataSet();
                final ItemStack done = itemDataSet.getData(ItemDataType.UPGRADE_DONE).get();
                final ItemStack notDone = itemDataSet.getData(ItemDataType.UPGRADE_NOT_DONE).get();

                if (level.getLevel().getType() == LevelType.LAST) {
                    return;
                }
                conditionSet.forEach(condition -> {
                    final int index = condition.getIndex();
                    if (condition.getConditionType() == ConditionType.ITEMS) {
                        final int amount = condition.getAmount();

                        final ItemStack stack = ItemUtils.notNull(inventory.getItem(index + 18));
                        if (stack.getType() == Material.AIR || amount >= condition.getNeed()) {
                            return;
                        }
                        final int stackAmount = stack.getAmount();
                        final int nowAmount = stackAmount + amount;

                        if (stack.getType() == condition.getType() && nowAmount >= amount) {
                            guildManager.updateConditions(guild, condition, Math.min(nowAmount, condition.getNeed()));
                            upgradeMenu.update(index, slot -> slot.updateStack(upgradeMenu.getItem(condition)));
                            upgradeMenu.update(index + 9, slot -> slot.updateStack(condition.isExecuted() ? done : notDone));
                            upgradeMenu.update(index + 18, slot -> {
                                final int newAmount = stackAmount - (condition.getNeed() - amount);
                                if(newAmount > 0) {
                                    final ItemStack itemStack = new ItemStack(stack.getType(), newAmount);
                                    itemStack.setAmount(newAmount);
                                    slot.updateStack(itemStack);
                                } else {
                                    slot.updateStack(null);
                                }
                            });
                        } else {
                            if(stack.getType() == condition.getType()) {
                                upgradeMenu.update(index + 18, slot -> slot.updateStack(null));
                            }
                        }
                        return;
                    }
                    upgradeMenu.update(index + 9, slot -> slot.updateStack(condition.isExecuted() ? done : notDone));
                });
                if (conditionSet.isExecuted() && !executed) {
                    executed = true;
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        guildManager.updateLevel(guild);
                        upgradeMenu.updateAll();
                        guild.broadcast(languageConfig.getMessage(Messages.GUILDS_LEVEL_UPDATE));
                        executed = false;
                    });
                }
            }
        });
    }

}
