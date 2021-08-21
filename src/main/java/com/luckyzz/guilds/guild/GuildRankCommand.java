package com.luckyzz.guilds.guild;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class GuildRankCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;

    GuildRankCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("grank"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.rank")) {
            return;
        }
        if (!operations.argumentsLength(2, languageConfig.getMessage(Messages.G_UPR_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> guildOptional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(guildOptional.isPresent(), languageConfig.getMessage(Messages.G_UPR_NOT_GUILD))) {
            return;
        }
        final String rankString = args[1];
        if (!operations.messageIfFalse(NumberUtils.isNumber(rankString), languageConfig.getMessage(Messages.G_UPR_NOT_DIGIT))) {
            return;
        }
        final int index = Integer.parseInt(rankString);
        if (!operations.messageIfFalse(index > 0 && index <= 4, languageConfig.getMessage(Messages.G_UPR_NOT_DIGIT))) {
            return;
        }

        final Guild guild = guildOptional.get();
        final GuildMemberList memberList = guild.getMemberList();
        final GuildMember senderMember = memberList.getMember(player).orElseThrow(IllegalArgumentException::new);
        if (!operations.messageIfFalse(senderMember.getRank().isPossibility(MemberPossibility.RANK),
                languageConfig.getMessage(Messages.G_UPR_NOT_OWNER))) {
            return;
        }

        final String name = args[0];
        if (!operations.messageIfFalse(!name.equals(player.getName()), languageConfig.getMessage(Messages.G_UPR_NOT_YOURSELF))) {
            return;
        }

        final Optional<GuildMember> memberOptional = memberList.getMember(name);
        if (!operations.messageIfFalse(memberOptional.isPresent(), languageConfig.getMessage(Messages.G_UPR_NOT_PLAYER))) {
            return;
        }
        final GuildMember member = memberOptional.get();
        final GuildRank rank = member.getRank();
        if (!operations.messageIfFalse(rank.getIndex() != index, languageConfig.getMessage(Messages.G_UPR_ALREADY_UPR))) {
            return;
        }

        guildManager.updateMember(guild, member, MemberRank.fromIndex(index));
        languageConfig.getMessage(Messages.G_UPR_SUCCESS_SENDER).send(sender);
        guild.broadcast(languageConfig.getAdaptiveMessage(Messages.G_UPR_SUCCESS_GUILD).placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", name));
    }

}
