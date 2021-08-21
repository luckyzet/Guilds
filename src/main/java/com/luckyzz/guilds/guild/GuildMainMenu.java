package com.luckyzz.guilds.guild;

import com.akamecoder.guilds.config.*;
import com.luckyzz.guilds.config.*;
import com.luckyzz.guilds.config.level.FeatureType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.inventory.InventorySize;
import com.akamecoder.menu.Menu;
import com.akamecoder.menu.MenuClicks;
import com.akamecoder.menu.MenuGenerator;
import com.akamecoder.message.Message;
import com.akamecoder.nravcoin.coin.PlayerBalanceApi;
import com.akamecoder.provider.economy.EconomyProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

class GuildMainMenu extends Menu {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final EconomyProvider economyProvider;
    private final PlayerBalanceApi balanceApi;
    private final GuildManager guildManager;
    private final Guild guild;

    GuildMainMenu(@NotNull final Player owner, @NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig,
                  @NotNull final EconomyProvider economyProvider, @NotNull final PlayerBalanceApi balanceApi,
                  @NotNull final GuildManager guildManager, @NotNull final Guild guild) {
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.economyProvider = economyProvider;
        this.balanceApi = balanceApi;
        this.guildManager = guildManager;
        this.guild = guild;
        this.generate();
        this.open(owner);
    }

    @Override
    protected void generate(@NotNull final MenuGenerator generator) {
        final ItemDataSet itemDataSet = optionConfig.getItemDataSet();
        final MenuData data = optionConfig.getMenuDataSet().getData(MenuDataType.MAIN);
        generator.title(data.getTitle().replace("%name%", guild.getIdentifier()));
        generator.size(InventorySize.SMALL_CHEST);

        final ItemStack pane = itemDataSet.getData(ItemDataType.PANE).get();
        for (int x = 0; x < 27; x++) {
            generator.slot(x, pane, MenuClicks.messageCancel(languageConfig.getMessage(Messages.MENU_PANE_CLICK)));
        }

        final ItemData closeData = itemDataSet.getData(ItemDataType.CLOSE);
        generator.slot(16, closeData.get(), MenuClicks.messageClose(languageConfig.getMessage(Messages.MENU_CLOSED)));

        final ItemStack unavailable = itemDataSet.getData(ItemDataType.UNAVAILABLE).get();

        final GuildLevel level = guild.getLevel();
        final ConfigFeatureSet featureSet = level.getFeatureSet();
        final Message unavailableMessage = languageConfig.getMessage(Messages.MENU_UNAVAILABLE);

        final GuildMenuSet menuSet = guild.getMenuSet();
        if (featureSet.getFeature(FeatureType.UPGRADE_GUILD).isPresent()) {
            generator.slot(11, itemDataSet.getData(ItemDataType.UPGRADE_GUILD).get(), click -> {
                GuildUpgradeMenu upgradeMenu = menuSet.getUpgradeMenu();
                if (upgradeMenu == null) {
                    upgradeMenu = new GuildUpgradeMenu(optionConfig, languageConfig, guild);
                    menuSet.updateUpgradeMenu(upgradeMenu);
                }
                upgradeMenu.open(click.getPlayer());
            });
        } else {
            generator.slot(11, unavailable, MenuClicks.messageCancel(unavailableMessage));
        }

        if (featureSet.getFeature(FeatureType.BANK).isPresent()) {
            generator.slot(10, itemDataSet.getData(ItemDataType.BANK).get(), click -> {
                GuildBankMenu bankMenu = menuSet.getBankMenu(1);
                if (bankMenu == null) {
                    bankMenu = new GuildBankMenu(1, optionConfig, languageConfig, economyProvider, guildManager, guild);
                    menuSet.registerBankMenu(bankMenu);
                }
                final Player got = click.getPlayer();
                final GuildMember member = guild.getMemberList().getMember(got).orElseThrow(IllegalArgumentException::new);
                if (!member.getRank().isPossibility(MemberPossibility.BANK)) {
                    languageConfig.getMessage(Messages.NOT_POSSIBILITY).send(got);
                    click.cancel(true);
                    return;
                }
                bankMenu.open(got);
            });
        } else {
            generator.slot(10, unavailable, MenuClicks.messageCancel(unavailableMessage));
        }

        if (featureSet.getFeature(FeatureType.STORE).isPresent()) {
            generator.slot(13, itemDataSet.getData(ItemDataType.STORE).get(), click -> {
                GuildStoreMenu storeMenu = menuSet.getStoreMenu(1);
                if (storeMenu == null) {
                    storeMenu = new GuildStoreMenu(1, optionConfig, languageConfig, economyProvider, balanceApi, guildManager, guild);
                    menuSet.registerStoreMenu(storeMenu);
                }
                storeMenu.open(click.getPlayer());
            });
        } else {
            generator.slot(13, unavailable, MenuClicks.messageCancel(unavailableMessage));
        }

        if (featureSet.getFeature(FeatureType.TROPHIES).isPresent()) {
            generator.slot(12, itemDataSet.getData(ItemDataType.TROPHIES).get(), click -> {
                GuildTrophiesMenu trophiesMenu = menuSet.getTrophiesMenu(1);
                if (trophiesMenu == null) {
                    trophiesMenu = new GuildTrophiesMenu(1, optionConfig, languageConfig, economyProvider, guildManager, guild);
                    menuSet.registerTrophiesMenu(trophiesMenu);
                }
                trophiesMenu.open(click.getPlayer());
            });
        } else {
            generator.slot(12, unavailable, MenuClicks.messageCancel(unavailableMessage));
        }
    }

}
