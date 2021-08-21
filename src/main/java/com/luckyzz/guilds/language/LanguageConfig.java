package com.luckyzz.guilds.language;

import com.akamecoder.config.MessageConfig;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LanguageConfig extends MessageConfig<Messages> {

    public LanguageConfig(@NotNull final Plugin plugin) {
        this.setup(builder -> builder.plugin(plugin).name("language").values(Messages.values()));
    }

}
