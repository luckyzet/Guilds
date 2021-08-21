package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.task.RegularTask;
import com.akamecoder.util.Lists;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

class RegularCooldownTask extends RegularTask {

    private final LeaveCooldownManager cooldownManager;
    private final LanguageConfig language;

    RegularCooldownTask(@NotNull final Plugin plugin, @NotNull final LanguageConfig language, @NotNull final LeaveCooldownManager cooldownManager) {
        super(plugin);
        this.cooldownManager = cooldownManager;
        this.language = language;
        this.runTaskTimerAsynchronously(10);
    }

    @Override
    public void run() {
        Lists.toList(cooldownManager.getCooldowns()).stream().filter(cooldown -> !cooldown.subtractTime()).forEach(cooldown -> {
            cooldownManager.remove(cooldown);
            cooldown.getOwner().ifPresent(player -> language.getMessage(Messages.COOLDOW_EXPIRED).send(player));
        });

        final GuildRepository repository = cooldownManager.getRepository();
        cooldownManager.getCooldowns().forEach(repository::updateCooldown);
    }

}
