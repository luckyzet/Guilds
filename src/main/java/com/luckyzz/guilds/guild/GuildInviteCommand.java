package com.luckyzz.guilds.guild;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.guild.request.GuildInviteManager;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class GuildInviteCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;
    private final GuildInviteManager inviteManager;
    private final LeaveCooldownManager cooldownManager;

    GuildInviteCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager,
                       @NotNull final GuildInviteManager inviteManager, @NotNull final LeaveCooldownManager cooldownManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("ginvite"));
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
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.ginvite")) {
            return;
        }
        if (!operations.argumentsLength(1, languageConfig.getMessage(Messages.G_INVITE_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> guildOptional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(guildOptional.isPresent(), languageConfig.getMessage(Messages.G_INVITE_NOT_GUILD))) {
            return;
        }
        final Guild guild = guildOptional.get();
        final GuildMemberList memberList = guild.getMemberList();

        final GuildMember senderMember = memberList.getMember(player).orElseThrow(IllegalArgumentException::new);
        final GuildRank senderRank = senderMember.getRank();
        if (!operations.messageIfFalse(senderRank.isPossibility(MemberPossibility.INVITE), languageConfig.getMessage(Messages.G_INVITE_NOT_UPR))) {
            return;
        }

        final String invited = args[0];
        if (!operations.messageIfFalse(!invited.equals(player.getName()), languageConfig.getMessage(Messages.G_INVITE_NOT_YOURSELF))) {
            return;
        }
        final Optional<GuildMember> memberOptional = memberList.getMember(invited);
        if (!operations.messageIfFalse(!memberOptional.isPresent(), languageConfig.getMessage(Messages.G_INVITE_ALREADY_IN_GUILD))) {
            return;
        }
        final Player invitedPlayer = Bukkit.getPlayerExact(invited);
        if (!operations.messageIfFalse(invitedPlayer != null, languageConfig.getMessage(Messages.G_INVITE_NOT_ONLINE))) {
            return;
        }
        if (!operations.messageIfFalse(!cooldownManager.contains(invitedPlayer), languageConfig.getMessage(Messages.COOLDOWN_CANNOT_ANOTHER))) {
            return;
        }

        inviteManager.unregisterRequestRequester(player);
        inviteManager.unregisterRequestRequested(invitedPlayer);
        inviteManager.registerRequest(player, invitedPlayer);
        languageConfig.getMessage(Messages.G_INVITE_SUCCESS_SENDER)
                .send(sender);
        languageConfig.getAdaptiveMessage(Messages.G_INVITE_SUCCESS_SENT)
                .placeholder("%guild%", guild.getIdentifier())
                .placeholder("%name%", player.getName())
                .send(invitedPlayer);
    }

}
