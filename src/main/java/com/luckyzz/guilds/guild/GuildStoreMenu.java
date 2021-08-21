package com.luckyzz.guilds.guild;

import com.akamecoder.guilds.config.*;
import com.luckyzz.guilds.config.*;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.inventory.InventorySize;
import com.akamecoder.itemstack.ItemUtils;
import com.akamecoder.menu.Menu;
import com.akamecoder.menu.MenuClicks;
import com.akamecoder.menu.MenuGenerator;
import com.akamecoder.nravcoin.NravCoinPlugin;
import com.akamecoder.nravcoin.coin.PlayerBalance;
import com.akamecoder.nravcoin.coin.PlayerBalanceApi;
import com.akamecoder.provider.economy.EconomyProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class GuildStoreMenu extends Menu {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final EconomyProvider economyProvider;
    private final PlayerBalanceApi balanceApi;
    private final GuildManager guildManager;
    private final Guild guild;
    private final int page;

    GuildStoreMenu(final int page, @NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig,
                   @NotNull final EconomyProvider economyProvider, @NotNull final PlayerBalanceApi balanceApi,
                   @NotNull final GuildManager guildManager, @NotNull final Guild guild) {
        this.page = page;
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.economyProvider = economyProvider;
        this.balanceApi = balanceApi;
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
        final MenuData data = optionConfig.getMenuDataSet().getData(MenuDataType.STORE);
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
            final Set<GuildStoreItem> storeItems = getItems(newPage);
            if (storeItems.size() == 0) {
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


        final ItemData storeData = itemData.getData(ItemDataType.STORE_ITEM);
        final Set<GuildStoreItem> items = getItems(page);

        for (int x = 0; x < 27; x++) {
            if (GuildStorageMenu.isPaneNextPrev(x)) {
                continue;
            }

            final GuildStoreItem item = getItem(items, x);
            if (item == null) {
                generator.slot(x, ItemUtils.air(), MenuClicks.cancel());
                continue;
            }
            final double money = item.getMoney();
            final ItemStack stack = storeData.apply(item.getStack().clone());
            final ItemMeta meta = stack.getItemMeta();

            final String displayName = meta.getDisplayName();
            final String price = (int) money + " " + (item.getCurrency() == CurrencyType.COINS ? "NC" : "$");
            if (displayName != null) {
                meta.setDisplayName(displayName.replace("%money%", price));
            }
            final List<String> lore = meta.getLore();
            if (lore != null) {
                final List<String> modified = new ArrayList<>();
                lore.forEach(got -> modified.add(got.replace("%money%", price)));
                meta.setLore(modified);
            }
            stack.setItemMeta(meta);

            generator.slot(item.getSlot(), stack, click -> {
                click.cancel(true);

                final Player player = click.getPlayer();
                final GuildMember got = guild.getMemberList().getMember(player).orElseThrow(IllegalArgumentException::new);
                if (!player.hasPermission("guilds.bypass") && !got.getRank().isPossibility(MemberPossibility.BUY_SHOP)) {
                    languageConfig.getMessage(Messages.NOT_POSSIBILITY).send(player);
                    return;
                }
                if(item.getCurrency() == CurrencyType.DEFAULT) {
                    if (!economyProvider.hasBalance(player, money)) {
                        languageConfig.getMessage(Messages.MENU_NOT_ENOUGH_MONEY).send(player);
                        return;
                    }
                    economyProvider.withdraw(player, money);
                    player.getInventory().addItem(item.getStack());
                    languageConfig.getMessage(Messages.MENU_SUCCESS_BOUGHT).send(player);
                    return;
                }
                if(item.getCurrency() == CurrencyType.COINS) {
                    balanceApi.getBalance(player).thenAccept(balance -> {
                        if(balance.getCoins() < money) {
                            languageConfig.getMessage(Messages.MENU_NOT_ENOUGH_MONEY).send(player);
                            return;
                        }
                        balance.modifyCoins(coins -> (long) (coins - money));
                        player.getInventory().addItem(item.getStack());
                        languageConfig.getMessage(Messages.MENU_SUCCESS_BOUGHT).send(player);
                    });
                }
            });
        }
    }

    private void openMenu(@NotNull final Player player, final int newPage) {
        final GuildMenuSet menuSet = guild.getMenuSet();

        GuildStoreMenu storeMenu = menuSet.getStoreMenu(newPage);
        if (storeMenu == null) {
            storeMenu = new GuildStoreMenu(newPage, optionConfig, languageConfig, economyProvider, balanceApi, guildManager, guild);
            menuSet.registerStoreMenu(storeMenu);
        }
        storeMenu.open(player);
    }

    @NotNull Set<GuildStoreItem> getItems(final int index) {
        return guild.getStoreList().stream()
                .filter(got -> index == got.getPage())
                .collect(Collectors.toSet());
    }

    @Nullable GuildStoreItem getItem(@NotNull final Set<GuildStoreItem> items, final int index) {
        return items.stream()
                .filter(item -> item.getSlot() == index)
                .findFirst().orElse(null);
    }

}
