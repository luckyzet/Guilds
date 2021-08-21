package com.luckyzz.guilds;

import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.guild.GuildManager;
import com.luckyzz.guilds.language.LanguageConfig;
import com.akamecoder.menu.MenuManager;
import com.akamecoder.nravcoin.coin.PlayerBalanceApi;
import com.akamecoder.provider.ProviderType;
import com.akamecoder.provider.Providers;
import com.akamecoder.provider.economy.EconomyProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuildsPlugin extends JavaPlugin {

    private GuildManager manager;

    @Override
    public void onEnable() {
        new MenuManager(this);

        final EconomyProvider economy = Providers.economy(ProviderType.VAULT);
        final PlayerBalanceApi balanceApi = Bukkit.getServicesManager().getRegistration(PlayerBalanceApi.class).getProvider();
        final OptionConfig optionConfig = new OptionConfig(this);
        final LanguageConfig languageConfig = new LanguageConfig(this);
        manager = new GuildManager(optionConfig, languageConfig, economy, balanceApi, this);
    }

    @Override
    public void onDisable() {
        try {
            manager.close();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

}
