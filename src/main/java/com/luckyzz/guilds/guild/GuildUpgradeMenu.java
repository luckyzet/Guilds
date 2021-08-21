package com.luckyzz.guilds.guild;

import com.akamecoder.guilds.config.*;
import com.luckyzz.guilds.config.level.ConditionType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.inventory.InventorySize;
import com.akamecoder.itemstack.ItemBuilder;
import com.akamecoder.itemstack.ItemBuilders;
import com.akamecoder.menu.Menu;
import com.akamecoder.menu.MenuClicks;
import com.akamecoder.menu.MenuGenerator;
import com.luckyzz.guilds.config.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class GuildUpgradeMenu extends Menu {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final Guild guild;

    GuildUpgradeMenu(@NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig, @NotNull final Guild guild) {
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.guild = guild;
        this.generate();
    }

    @Override
    protected void generate(@NotNull final MenuGenerator generator) {
        final ItemDataSet itemDataSet = optionConfig.getItemDataSet();
        final MenuData data = optionConfig.getMenuDataSet().getData(MenuDataType.UPGRADE);
        generator.title(data.getTitle());
        generator.size(InventorySize.SMALL_CHEST);

        final ItemStack barrier = itemDataSet.getData(ItemDataType.UPGRADE_BARRIER).get();
        for (int x = 0; x < 27; x++) {
            generator.slot(x, barrier, MenuClicks.messageCancel(languageConfig.getMessage(Messages.MENU_PANE_CLICK)));
        }

        final GuildLevel level = guild.getLevel();
        final ExecutingConditionSet conditionSet = level.getConditionSet();

        final ItemStack done = itemDataSet.getData(ItemDataType.UPGRADE_DONE).get();
        final ItemStack notDone = itemDataSet.getData(ItemDataType.UPGRADE_NOT_DONE).get();

        conditionSet.forEach(executingCondition -> {
            final ConditionType conditionType = executingCondition.getConditionType();
            final int index = executingCondition.getIndex();
            final int need = executingCondition.getNeed();
            final int amount = executingCondition.getAmount();

            if (conditionType == ConditionType.ITEMS) {
                final ItemExecutingCondition condition = (ItemExecutingCondition) executingCondition;
                generator.slot(index, getItem(condition), MenuClicks.cancel());
                generator.slot(index + 9, amount >= need ? done : notDone, MenuClicks.cancel());
                if(holder.getInventory() != null) {
                    final ItemStack stack = holder.getInventory().getItem(index + 18);
                    generator.slot(index + 18, stack, click -> {
                        final Player got = click.getPlayer();
                        if(!got.hasPermission("guilds.bypass")) {
                            final GuildMember member = guild.getMemberList().getMember(got).orElseThrow(IllegalArgumentException::new);
                            click.cancel(!member.getRank().isPossibility(MemberPossibility.UPGRADE));
                        } else {
                            click.cancel(false);
                        }
                    });
                } else {
                    generator.slot(index + 18, (ItemStack) null, click -> {
                        final Player got = click.getPlayer();
                        if(!got.hasPermission("guilds.bypass")) {
                            final GuildMember member = guild.getMemberList().getMember(got).orElseThrow(IllegalArgumentException::new);
                            click.cancel(!member.getRank().isPossibility(MemberPossibility.UPGRADE));
                        } else {
                            click.cancel(false);
                        }
                    });
                }
                return;
            }
            final EntityExecutingCondition condition = (EntityExecutingCondition) executingCondition;
            final ItemStack stack = getItem(condition);
            generator.slot(index, stack, MenuClicks.cancel());
            generator.slot(index + 9, amount >= need ? done : notDone, MenuClicks.cancel());
            generator.slot(index + 18, stack, MenuClicks.cancel());
        });
    }

    @NotNull ItemStack getItem(@NotNull final ExecutingLevelCondition<?> executingCondition) {
        if (executingCondition.getConditionType() == ConditionType.MOBS) {
            final EntityExecutingCondition condition = (EntityExecutingCondition) executingCondition;
            final ItemData data = optionConfig.getItemDataSet().getData(ItemDataType.UPGRADE_ENTITY);
            final EntityHeadSet headSet = optionConfig.getHeadSet();
            final EntityHead head = headSet.getHead(condition.getType());
            if (head.getHeadType() == EntityHeadType.SKULL) {
                return operate(head, data, ItemBuilders.skullBuilder().skull(head.getSkull()), condition);
            }
            return operate(head, data, ItemBuilders.defaultBuilder().material(head.getMaterial()), condition);
        }
        final ItemExecutingCondition condition = (ItemExecutingCondition) executingCondition;
        final ItemData data = optionConfig.getItemDataSet().getData(ItemDataType.UPGRADE_ITEM);
        return operate(data, ItemBuilders.defaultBuilder().material(condition.getType()), condition);
    }

    private <T extends ItemBuilder<?>> @NotNull ItemStack operate(@NotNull final EntityHead head, @NotNull final ItemData data,
                                                                  @NotNull final T builder, @NotNull final EntityExecutingCondition condition) {
        final String headDisplay = head.getDisplayName();
        if (headDisplay != null) {
            builder.displayName(headDisplay);
        }
        return operate(data, builder, condition);
    }

    private <T extends ItemBuilder<?>> @NotNull ItemStack operate(@NotNull final ItemData data, @NotNull final T builder,
                                                                  @NotNull final ExecutingLevelCondition<?> condition) {
        final String dataDisplay = data.getDisplayName();
        if (dataDisplay != null) {
            builder.displayName(dataDisplay.replace("%material%", condition.getType().toString()));
        }
        final List<String> lore = data.getLore();
        if (lore != null) {
            final List<String> modified = new ArrayList<>();
            lore.forEach(got -> {
                final String result = got.replace("%need%", String.valueOf(condition.getNeed()))
                        .replace("%amount%", String.valueOf(condition.getAmount()));
                modified.add(result);
            });
            builder.lore(modified);
        }
        return builder.create();
    }

}
