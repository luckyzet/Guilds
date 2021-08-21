package com.luckyzz.guilds.guild;

import com.akamecoder.api.Cancelable;
import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.config.Options;
import com.luckyzz.guilds.guild.cooldown.LeaveCooldown;
import com.luckyzz.guilds.language.LanguageConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class LeaveCooldownManager implements Cancelable {

    private final List<LeaveCooldown> cooldowns;
    private final OptionConfig option;
    private final GuildRepository repository;
    private final RegularCooldownTask cooldownTask;

    LeaveCooldownManager(@NotNull final Plugin plugin, @NotNull final OptionConfig option, @NotNull final LanguageConfig language,
                         @NotNull final GuildRepository repository) {
        this.cooldowns = repository.loadCooldowns();
        this.option = option;
        this.repository = repository;
        this.cooldownTask = new RegularCooldownTask(plugin, language, this);
    }

    @NotNull List<LeaveCooldown> getCooldowns() {
        return cooldowns;
    }

    @NotNull GuildRepository getRepository() {
        return repository;
    }

    public @NotNull Stream<LeaveCooldown> stream() {
        return cooldowns.stream();
    }

    public boolean contains(@NotNull final Player player) {
        return cooldowns.stream().anyMatch(cooldown -> cooldown.getIdentifier().equals(player.getName()));
    }

    public void add(@NotNull final Player player) {
        final LeaveCooldown cooldown = new LeaveCooldownImpl(player.getName(), option.getOption(Options.TIME_COOLDOWN));
        cooldowns.add(cooldown);
        repository.createCooldown(cooldown);
    }

    void remove(@NotNull final LeaveCooldown cooldown) {
        cooldowns.remove(cooldown);
        repository.removeCooldown(cooldown);
    }

    @Override
    public void cancel() {
        cooldownTask.cancel();
    }

    @Override
    public @NotNull String toString() {
        return "LeaveCooldownList{" +
                "cooldowns=" + cooldowns +
                '}';
    }

}
