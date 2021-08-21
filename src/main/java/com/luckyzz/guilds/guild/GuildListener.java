package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.config.level.ConditionType;
import com.luckyzz.guilds.config.level.LevelType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.listener.Listener;
import com.akamecoder.menu.Menu;
import com.akamecoder.menu.MenuHolder;
import com.akamecoder.nravcoin.coin.PlayerBalanceApi;
import com.akamecoder.provider.economy.EconomyProvider;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

class GuildListener extends Listener {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final EconomyProvider economyProvider;
    private final PlayerBalanceApi balanceApi;
    private final GuildManager guildManager;

    GuildListener(@NotNull final Plugin plugin, @NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig,
                  @NotNull final EconomyProvider economyProvider, @NotNull final PlayerBalanceApi balanceApi,
                  @NotNull final GuildManager guildManager) {
        super(plugin);
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.economyProvider = economyProvider;
        this.balanceApi = balanceApi;
        this.guildManager = guildManager;
    }

    @EventHandler
    public void onClick(@NotNull final InventoryClickEvent event) {
        final Entity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) {
            return;
        }
        final Player player = (Player) entity;

        final Inventory top = event.getView().getTopInventory();
        final InventoryHolder holder = top.getHolder();
        if (!(holder instanceof MenuHolder)) {
            return;
        }
        final Menu menu = ((MenuHolder) holder).getMenu();
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (menu instanceof GuildStoreMenu) {
                event.setCancelled(true);
                return;
            }
            if (menu instanceof GuildTrophiesMenu) {
                final Guild guild = guildManager.getGuildSet().getGuild(player).orElseThrow(IllegalArgumentException::new);
                final GuildMember member = guild.getMemberList().getMember(player).orElse(null);
                if (member == null) {
                    throw new NullPointerException();
                }

                event.setCancelled(!member.getRank().isPossibility(MemberPossibility.TROPHIES));
            }
        }
    }

    @EventHandler
    public void onInteract(@NotNull final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        final Player player = event.getPlayer();
        guildManager.getGuildSet().getGuild(block.getLocation()).ifPresent(guild -> {
            event.setCancelled(true);
            if (!guild.getMemberList().getMember(player).isPresent() && !player.hasPermission("guilds.bypass")) {
                languageConfig.getMessage(Messages.MENU_FOR_MEMBERS).send(player);
                return;
            }
            new GuildMainMenu(player, optionConfig, languageConfig, economyProvider, balanceApi, guildManager, guild);
        });
    }

    @EventHandler
    public void onEntityDeath(@NotNull final EntityDeathEvent event) {
        final Player player = event.getEntity().getKiller();
        final Entity entity = event.getEntity();

        if (player == null) {
            return;
        }

        final Guild guild = guildManager.getGuildSet().getGuild(player).orElse(null);
        if (guild == null) {
            return;
        }
        final GuildLevel level = guild.getLevel();
        final ExecutingConditionSet conditionSet = level.getConditionSet();
        if (level.getLevel().getType() == LevelType.LAST) {
            return;
        }
        final GuildUpgradeMenu upgradeMenu = guild.getMenuSet().getUpgradeMenu();
        conditionSet.stream().filter(condition -> condition.getConditionType() == ConditionType.MOBS)
                .map(condition -> (EntityExecutingCondition) condition)
                .filter(condition -> condition.getType() == entity.getType())
                .findFirst().ifPresent(condition -> {
            final int index = condition.getIndex();
            final int amount = condition.getAmount();
            guildManager.updateConditions(guild, condition, amount + 1);

            if (upgradeMenu != null) {
                final ItemStack got = upgradeMenu.getItem(condition);
                upgradeMenu.update(index, slot -> slot.updateStack(got));
                upgradeMenu.update(index + 18, slot -> slot.updateStack(got));
            }
        });
    }

}
