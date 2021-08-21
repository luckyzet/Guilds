package com.luckyzz.guilds.guild;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.util.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

class GuildChatCommand extends ChatCommand {

    private final LanguageConfig languageConfig;
    private final GuildManager guildManager;

    GuildChatCommand(@NotNull final LanguageConfig languageConfig, @NotNull final GuildManager guildManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("g"));
        this.languageConfig = languageConfig;
        this.guildManager = guildManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.sender(ChatOperations.SenderType.PLAYER, languageConfig.getMessage(Messages.ONLY_FOR_PLAYER))) {
            return;
        }
        if (!operations.permission(languageConfig.getMessage(Messages.NO_PERMISSION), "guilds.chat")) {
            return;
        }
        if (!operations.argumentsMoreThan(0, languageConfig.getMessage(Messages.G_CHAT_USAGE))) {
            return;
        }

        final Player player = operations.player();
        final Optional<Guild> optional = guildManager.getGuildSet().getGuild(player);
        if (!operations.messageIfFalse(optional.isPresent(), languageConfig.getMessage(Messages.G_CHAT_NOT_GUILD))) {
            return;
        }
        final Guild guild = optional.get();
        final GuildMember member = guild.getMemberList().getMember(player).orElseThrow(IllegalArgumentException::new);
        if (!operations.messageIfFalse(member.getRank().isPossibility(MemberPossibility.CHAT), languageConfig.getMessage(Messages.NOT_POSSIBILITY))) {
            return;
        }

        guild.broadcast(languageConfig.getAdaptiveMessage(Messages.G_CHAT_FORMAT)
                .placeholder("%name%", player.getName())
                .placeholder("%prefix%", member.getRank().getPrefix())
                .placeholder("%message%", StringUtils.combineWithSpace(args)));
    }

}
