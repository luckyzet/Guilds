package com.luckyzz.guilds.guild;

import com.akamecoder.color.ColorUtils;
import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.message.AdaptiveMessage;
import com.akamecoder.util.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class GuildListCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;

    GuildListCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("glist"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.list")) {
            return;
        }
        if (!operations.argumentsLength(0, languageConfig.getMessage(Messages.G_LIST_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> optional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(optional.isPresent(), languageConfig.getMessage(Messages.G_LIST_NOT_GUILD))) {
            return;
        }
        final Guild guild = optional.get();
        final GuildMember member = guild.getMemberList().getMember(player).orElseThrow(IllegalArgumentException::new);
        if (!operations.messageIfFalse(member.getRank().isPossibility(MemberPossibility.LIST), languageConfig.getMessage(Messages.NOT_POSSIBILITY))) {
            return;
        }

        languageConfig.getMessage(Messages.G_LIST_FORMAT_TOP).send(sender);

        for(final MemberRank rank : MemberRank.values()) {
            final GuildRank guildRank = guild.getRankSet().get(rank.getIndex());
            final List<GuildMember> members = guild.getMemberList().stream()
                    .filter(got -> got.getRank().getIndex() == guildRank.getIndex())
                    .collect(Collectors.toList());
            if(!members.isEmpty()) {
                final AdaptiveMessage message = languageConfig.getAdaptiveMessage(Messages.G_LIST_FORMAT_RANK)
                        .placeholder("%rank%", guildRank.getPrefix());
                final StringBuilder builder = new StringBuilder();
                for(int i = 0; i < members.size(); i++) {
                    final GuildMember got = members.get(i);
                    builder.append(ColorUtils.DEFAULT_CODE + "c").append(got.getIdentifier());
                    if((i + 1) != members.size()) {
                        builder.append(ColorUtils.DEFAULT_CODE + "f, ");
                    }
                }
                message.placeholder("%members%", builder.toString()).send(sender);
            }
        }
    }

}
