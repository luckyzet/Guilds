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

class GuildKickCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;

    GuildKickCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("gkick"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.kick")) {
            return;
        }
        if (!operations.argumentsLength(1, languageConfig.getMessage(Messages.G_KICK_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> guildOptional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(guildOptional.isPresent(), languageConfig.getMessage(Messages.G_KICK_NOT_GUILD))) {
            return;
        }
        final Guild guild = guildOptional.get();
        final GuildMemberList memberList = guild.getMemberList();

        final GuildMember senderMember = memberList.getMember(player).orElseThrow(IllegalArgumentException::new);
        final GuildRank senderRank = senderMember.getRank();
        if (!operations.messageIfFalse(senderRank.isPossibility(MemberPossibility.KICK), languageConfig.getMessage(Messages.G_KICK_NOT_UPR))) {
            return;
        }

        final String name = args[0];
        if (!operations.messageIfFalse(!name.equals(player.getName()), languageConfig.getMessage(Messages.G_KICK_NOT_YOURSELF))) {
            return;
        }
        final Optional<GuildMember> memberOptional = memberList.getMember(name);
        if (!operations.messageIfFalse(memberOptional.isPresent(), languageConfig.getMessage(Messages.G_KICK_NOT_PLAYER))) {
            return;
        }
        final GuildMember member = memberOptional.get();
        final GuildRank rank = member.getRank();
        if (!operations.messageIfFalse(!senderRank.isLess(rank), languageConfig.getMessage(Messages.G_KICK_PLAYER_UPR))) {
            return;
        }
        guild.broadcast(languageConfig.getAdaptiveMessage(Messages.G_KICK_SUCCESS_GUILD)
                .placeholder("%name%", name)
                .placeholder("%upr%", player.getName()));
        guildManager.unregisterMember(guild, member);

        languageConfig.getMessage(Messages.G_KICK_SUCCESS_SENDER).send(sender);
    }

}
