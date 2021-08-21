package com.luckyzz.guilds.guild;

import com.akamecoder.color.ColorUtils;
import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.config.level.ConfigLevel;
import com.luckyzz.guilds.guild.request.GuildInviteManager;
import com.luckyzz.guilds.language.LanguageConfig;
import com.akamecoder.nravcoin.coin.PlayerBalanceApi;
import com.akamecoder.provider.economy.EconomyProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class GuildManager implements AutoCloseable {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final EconomyProvider economyProvider;
    private final PlayerBalanceApi balanceApi;

    private final GuildRepository repository;
    private final GuildSet guildSet;

    private final RegularGuildTask regularTask;
    private final GuildUpgradeTask upgradeTask;
    private final GuildInviteManager inviteManager;

    private final LeaveCooldownManager cooldownManager;

    public GuildManager(@NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig,
                        @NotNull final EconomyProvider economyProvider, @NotNull final PlayerBalanceApi balanceApi, @NotNull final Plugin plugin) {
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.economyProvider = economyProvider;
        this.balanceApi = balanceApi;

        repository = new DatabaseGuildRepository(optionConfig, plugin);
        guildSet = repository.loadGuilds(optionConfig);
        cooldownManager = new LeaveCooldownManager(plugin, optionConfig, languageConfig, repository);

        new GuildCreateCommand(languageConfig, this);
        new GuildChatCommand(languageConfig, this);
        inviteManager = new GuildInviteManager(plugin, optionConfig, languageConfig);
        new GuildInviteCommand(languageConfig, this, inviteManager, cooldownManager);
        new GuildAcceptCommand(languageConfig, this, inviteManager, cooldownManager);
        new GuildKickCommand(languageConfig, this);
        new GuildLeaveCommand(languageConfig, this, cooldownManager);
        new GuildRankCommand(languageConfig, this);
        new GuildTitleCommand(languageConfig, this);
        new GuildListCommand(languageConfig, this);
        new GuildListener(plugin, optionConfig, languageConfig, economyProvider, balanceApi, this);

        regularTask = new RegularGuildTask(plugin, this);
        upgradeTask = new GuildUpgradeTask(plugin, optionConfig, languageConfig, this);

        Bukkit.getServicesManager().register(GuildManager.class, this, plugin, ServicePriority.Highest);
    }

    public @NotNull GuildSet getGuildSet() {
        return guildSet;
    }

    public void createGuild(@NotNull final String identifier, @NotNull final String owner, @NotNull final Location location) {
        final ConfigLevel level = optionConfig.getLevelSet().getDefaultLevel().orElseThrow(IllegalArgumentException::new);
        final Guild guild = new GuildImpl(this, optionConfig, ColorUtils.color(identifier), location, owner, level, GuildRank.defaultRanks());
        guildSet.registerGuild(guild);
        repository.createGuild(guild);
    }

    public void registerMember(@NotNull final Guild guild, @NotNull final Player player) {
        final GuildMember member = new GuildMemberImpl(player.getName(), guild.getRankSet().get(1));
        guild.getMemberList().registerMember(member);
        guild.getPoint().updateHologramData(guild);
        repository.registerMember(guild, member);
    }

    public void unregisterMember(@NotNull final Guild guild, @NotNull final GuildMember member) {
        guild.getMemberList().unregisterMember(member);
        guild.getPoint().updateHologramData(guild);
        repository.unregisterMember(guild, member);
    }

    public void updateMember(@NotNull final Guild guild, @NotNull final GuildMember member, @NotNull final MemberRank rank) {
        final GuildRank got = guild.getRankSet().get(rank.getIndex());
        member.updateRank(got);
        repository.updateMemberRank(guild, member);
    }

    public void updateRank(@NotNull final Guild guild, @NotNull final GuildRank rank, @NotNull final String prefix) {
        rank.setPrefix(prefix);
        repository.updateRank(guild, rank);
    }

    //

    public void updateConditions(@NotNull final Guild guild, @NotNull final ExecutingLevelCondition<?> condition, final int amount) {
        condition.setAmount(amount);
        repository.updateConditions(guild, condition);
    }

    public void updateLevel(@NotNull final Guild guild) {
        guild.getLevel().getNextLevel().ifPresent(next -> {
            guild.updateLevel(this, next);
            guild.getPoint().updateHologramData(guild);
            repository.updateLevel(guild);
        });
    }

    public void registerStoreItem(@NotNull final Guild guild, @NotNull final GuildStoreItem storeItem) {
        guild.getStoreList().registerStoreItem(storeItem);

        final int index = (storeItem.getIndex() / 26) + 1;
        final GuildMenuSet menuSet = guild.getMenuSet();
        GuildStoreMenu storeMenu = menuSet.getStoreMenu(index);
        if (storeMenu == null) {
            storeMenu = new GuildStoreMenu(index, optionConfig, languageConfig, economyProvider, balanceApi, this, guild);
            menuSet.registerStoreMenu(storeMenu);
        }
        storeMenu.updateAll();
    }

    //

    public void updateBankPage(@NotNull final Guild guild, @NotNull final GuildBankMenu bankMenu,
                               @NotNull final Set<GuildItem> newItems) {
        final GuildItemList bankList = guild.getBankList();
        final Set<GuildItem> items = bankMenu.getItems(bankMenu.getPage());
        bankList.unregisterItems(items);
        bankList.registerItems(newItems);
        repository.updateItems(guild, "bank", items, newItems);
    }

    public void updateTrophiesPage(@NotNull final Guild guild, @NotNull final GuildTrophiesMenu trophiesMenu,
                                   @NotNull final Set<GuildItem> newItems) {
        final GuildItemList trophiesList = guild.getTrophiesList();
        final Set<GuildItem> items = trophiesMenu.getItems(trophiesMenu.getPage());
        trophiesList.unregisterItems(items);
        trophiesList.registerItems(newItems);
        repository.updateItems(guild, "trophies", items, newItems);
    }

    @Override
    public void close() throws Exception {
        regularTask.cancel();
        regularTask.run();
        upgradeTask.cancel();
        repository.close();
        inviteManager.cancel();
        cooldownManager.cancel();
        guildSet.forEach(guild -> guild.getPoint().remove());
    }

}
