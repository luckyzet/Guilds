package com.luckyzz.guilds.guild;

import com.akamecoder.guilds.config.*;
import com.luckyzz.guilds.config.*;
import com.luckyzz.guilds.config.level.FeatureType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.inventory.InventorySize;
import com.akamecoder.itemstack.ItemUtils;
import com.akamecoder.menu.Menu;
import com.akamecoder.menu.MenuClicks;
import com.akamecoder.menu.MenuGenerator;
import com.akamecoder.provider.economy.EconomyProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

class GuildBankMenu extends Menu {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final EconomyProvider economyProvider;
    private final GuildManager guildManager;
    private final Guild guild;
    private final int page;

    GuildBankMenu(final int page, @NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig,
                  @NotNull final EconomyProvider economyProvider, @NotNull final GuildManager guildManager, @NotNull final Guild guild) {
        this.page = page;
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.economyProvider = economyProvider;
        this.guildManager = guildManager;
        this.guild = guild;
        this.generate();
    }

    public int getPage() {
        return page;
    }

    @Override
    protected void generate(@NotNull final MenuGenerator generator) {
        final ItemDataSet itemData = optionConfig.getItemDataSet();
        final MenuData data = optionConfig.getMenuDataSet().getData(MenuDataType.BANK);
        generator.title(data.getTitle().replace("%page%", String.valueOf(page)));
        generator.size(InventorySize.SMALL_CHEST);

        final ItemStack pane = itemData.getData(ItemDataType.PANE).get();
        for (final int got : GuildStorageMenu.panes) {
            generator.slot(got, pane, MenuClicks.messageCancel(languageConfig.getMessage(Messages.MENU_PANE_CLICK)));
        }

        generator.slot(GuildStorageMenu.next, itemData.getData(ItemDataType.NEXT_PAGE).get(), click -> {
            click.cancel(true);

            final Player player = click.getPlayer();
            final int newPage = page + 1;

            final int sumPages = guild.getLevel().getFeatureSet().stream()
                    .filter(feature -> feature.getType() == FeatureType.EXTRA_BANK_PAGES)
                    .map(feature -> (AmountLevelFeature) feature)
                    .mapToInt(AmountLevelFeature::getAmount)
                    .sum();
            if (newPage > sumPages) {
                languageConfig.getMessage(Messages.MENU_PAGE_UNAVAILABLE).send(player);
                return;
            }

            openMenu(player, newPage);
        });

        generator.slot(GuildStorageMenu.prev, itemData.getData(ItemDataType.PREV_PAGE).get(), click -> {
            click.cancel(true);

            final Player player = click.getPlayer();
            final int newPage = page - 1;

            if (newPage <= 0) {
                languageConfig.getMessage(Messages.MENU_PAGE_UNAVAILABLE).send(player);
                return;
            }

            openMenu(player, newPage);
        });

        final Set<GuildItem> items = getItems(page);
        for (int x = 0; x < 27; x++) {
            if (GuildStorageMenu.isPaneNextPrev(x)) {
                continue;
            }
            final GuildItem item = getItem(items, x);
            generator.slot(item.getSlot(), item.getStack(), click -> click.cancel(false));
        }
    }

    private void openMenu(@NotNull final Player player, final int newPage) {
        final GuildMenuSet menuSet = guild.getMenuSet();

        GuildBankMenu bankMenu = menuSet.getBankMenu(newPage);
        if (bankMenu == null) {
            bankMenu = new GuildBankMenu(newPage, optionConfig, languageConfig, economyProvider, guildManager, guild);
            menuSet.registerBankMenu(bankMenu);
        }
        bankMenu.open(player);
    }

    @NotNull Set<GuildItem> getItems(final int index) {
        return guild.getBankList().stream()
                .filter(got -> index == got.getPage())
                .collect(Collectors.toSet());
    }

    @NotNull GuildItem getItem(@NotNull final Set<GuildItem> items, final int index) {
        return items.stream()
                .filter(item -> item.getSlot() == index)
                .findFirst().orElse(new GuildItemImpl(index + ((page - 1) * 26), ItemUtils.air()));
    }

}
