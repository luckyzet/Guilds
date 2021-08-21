package com.luckyzz.guilds.guild;

import com.akamecoder.color.ColorUtils;
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

class GuildTitleCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;

    GuildTitleCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("gtitle"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.title")) {
            return;
        }
        if (!operations.argumentsLength(2, languageConfig.getMessage(Messages.G_TITLE_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> guildOptional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(guildOptional.isPresent(), languageConfig.getMessage(Messages.G_TITLE_NOT_GUILD))) {
            return;
        }
        final String rankString = args[0];
        if (!operations.messageIfFalse(NumberUtils.isNumber(rankString), languageConfig.getMessage(Messages.G_TITLE_NOT_DIGIT))) {
            return;
        }
        final int index = Integer.parseInt(rankString);
        if (!operations.messageIfFalse(index > 0 && index <= 4, languageConfig.getMessage(Messages.G_TITLE_NOT_DIGIT))) {
            return;
        }

        final Guild guild = guildOptional.get();
        final GuildMemberList memberList = guild.getMemberList();
        final GuildMember senderMember = memberList.getMember(player).orElseThrow(IllegalArgumentException::new);
        if (!operations.messageIfFalse(senderMember.getRank().isPossibility(MemberPossibility.TITLE),
                languageConfig.getMessage(Messages.G_TITLE_NOT_UPR))) {
            return;
        }

        final String prefix = ColorUtils.color(args[1]);
        final GuildRank rank = guild.getRankSet().get(index);
        if (!operations.messageIfFalse(!rank.getPrefix().equals(prefix), languageConfig.getMessage(Messages.G_TITLE_ALREADY))) {
            return;
        }

        final String old = rank.getPrefix();
        guildManager.updateRank(guild, rank, prefix);
        languageConfig.getMessage(Messages.G_TITLE_SUCCESS_SENDER).send(sender);
        guild.broadcast(languageConfig.getAdaptiveMessage(Messages.G_TITLE_SUCCESS_GUILD)
                .placeholder("%name%", player.getName())
                .placeholder("%old%", old)
                .placeholder("%new%", prefix));
    }

}
