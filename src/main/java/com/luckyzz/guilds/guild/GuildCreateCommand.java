package com.luckyzz.guilds.guild;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class GuildCreateCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;

    GuildCreateCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("gcreate"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.create")) {
            return;
        }
        if (!operations.argumentsMoreThan(1, languageConfig.getMessage(Messages.G_CREATE_USAGE))) {
            return;
        }
        final Player owner = Bukkit.getPlayerExact(args[0]);
        final String identifier = StringUtils.combineWithSpace((String[]) ArrayUtils.remove(args, 0));

        guildManager.createGuild(identifier, args[0], operations.player().getLocation());
        if (owner != null) {
            languageConfig.getMessage(Messages.G_CREATE_SUCCESS_OWNER).send(owner);
        }
        languageConfig.getMessage(Messages.G_CREATE_SUCCESS_ADMIN).send(sender);
    }

}
