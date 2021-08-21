package com.luckyzz.guilds.guild;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.guild.request.GuildInviteManager;
import com.luckyzz.guilds.guild.request.GuildInviteRequest;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class GuildAcceptCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;
    private final GuildInviteManager inviteManager;
    private final LeaveCooldownManager cooldownManager;

    GuildAcceptCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager,
                       @NotNull final GuildInviteManager inviteManager, @NotNull final LeaveCooldownManager cooldownManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("gaccept"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
        this.inviteManager = inviteManager;
        this.cooldownManager = cooldownManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.accept")) {
            return;
        }
        if (!operations.argumentsLength(0, languageConfig.getMessage(Messages.G_ACCEPT_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> guildOptional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(!guildOptional.isPresent(), languageConfig.getMessage(Messages.G_ACCEPT_ALREADY_GUILD))) {
            return;
        }

        final Optional<GuildInviteRequest> requestOptional = inviteManager.getRequest(player);
        if (!operations.messageIfFalse(requestOptional.isPresent(), languageConfig.getMessage(Messages.G_ACCEPT_NOT_INVITES))) {
            return;
        }
        final GuildInviteRequest request = requestOptional.get();

        if (!operations.messageIfFalse(!cooldownManager.contains(request.getRequested()), languageConfig.getMessage(Messages.COOLDOWN_CANNOT_WHO_GOT))) {
            return;
        }

        final Guild guild = guildManager.getGuildSet().getGuild(request.getRequester()).orElseThrow(IllegalArgumentException::new);
        inviteManager.unregisterRequest(request);
        guildManager.registerMember(guild, request.getRequested());

        languageConfig.getMessage(Messages.G_ACCEPT_SUCCESS_SENDER).send(sender);
        guild.broadcast(languageConfig.getAdaptiveMessage(Messages.G_ACCEPT_SUCCESS_GUILD).placeholder("%name%", player.getName()));
    }

}
