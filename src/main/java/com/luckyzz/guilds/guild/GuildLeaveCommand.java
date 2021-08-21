package com.luckyzz.guilds.guild;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class GuildLeaveCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;
    private final LeaveCooldownManager cooldownManager;

    GuildLeaveCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager, @NotNull final LeaveCooldownManager cooldownManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("gleave"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
        this.cooldownManager = cooldownManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.leave")) {
            return;
        }
        if (!operations.argumentsLength(0, languageConfig.getMessage(Messages.G_LEAVE_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> guildOptional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(guildOptional.isPresent(), languageConfig.getMessage(Messages.G_LEAVE_NOT_GUILD))) {
            return;
        }
        final Guild guild = guildOptional.get();
        final GuildMember member = guild.getMemberList().getMember(player).orElseThrow(IllegalArgumentException::new);
        if (!operations.messageIfFalse(member.getRank().getIndex() != 5, languageConfig.getMessage(Messages.G_LEAVE_CANNOT))) {
            return;
        }
        guildManager.unregisterMember(guild, member);

        cooldownManager.add(player);
        languageConfig.getMessage(Messages.G_LEAVE_SUCCESS_SENDER).send(sender);
        guild.broadcast(languageConfig.getAdaptiveMessage(Messages.G_LEAVE_SUCCESS_GUILD).placeholder("%name%", player.getName()));
    }

}
