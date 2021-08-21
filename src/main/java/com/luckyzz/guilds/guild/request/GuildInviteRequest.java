package com.luckyzz.guilds.guild.request;

import com.luckyzz.guilds.language.LanguageConfig;
import com.luckyzz.guilds.language.Messages;
import com.akamecoder.request.AbstractRequestContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GuildInviteRequest extends AbstractRequestContext {

    GuildInviteRequest(@NotNull final LanguageConfig languageConfig, final long time, @NotNull final Player requester, @NotNull final Player requested) {
        super(time, requester, requested, (g1, g2) -> {
            languageConfig.getMessage(Messages.G_INVITE_REQUEST_NOT_ACCEPTED_REQUESTED)
                    .send(requested);

            languageConfig.getAdaptiveMessage(Messages.G_INVITE_REQUEST_NOT_ACCEPTED_REQUESTER)
                    .placeholder("%name%", requested.getName())
                    .send(requester);
        });
    }

    @Override
    public @NotNull String toString() {
        return "GuildInviteRequest{} " + super.toString();
    }

}
