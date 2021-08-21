package com.luckyzz.guilds.guild.request;

import com.akamecoder.api.Cancelable;
import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.config.Options;
import com.luckyzz.guilds.language.LanguageConfig;
import com.akamecoder.request.RequestContext;
import com.akamecoder.request.RequestContextManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("all")
public final class GuildInviteManager implements Cancelable {

    private final OptionConfig optionConfig;
    private final LanguageConfig languageConfig;
    private final RequestContextManager contextManager;

    public GuildInviteManager(@NotNull final Plugin plugin, @NotNull final OptionConfig optionConfig, @NotNull final LanguageConfig languageConfig) {
        this.optionConfig = optionConfig;
        this.languageConfig = languageConfig;
        this.contextManager = new RequestContextManager(plugin);
    }

    private void unregisterRequest(@NotNull final Optional<RequestContext> optional) {
        optional.ifPresent(request -> contextManager.unregisterRequestContext(request));
    }

    public void unregisterRequestRequester(@NotNull final Player player) {
        unregisterRequest(contextManager.getRequestByRequester(player));
    }

    public void unregisterRequestRequested(@NotNull final Player player) {
        unregisterRequest(contextManager.getRequestByRequested(player));
    }

    public void unregisterRequest(@NotNull final GuildInviteRequest request) {
        contextManager.unregisterRequestContext(request);
    }

    public void registerRequest(@NotNull final Player requester, @NotNull final Player requested) {
        final int time = optionConfig.getOption(Options.TIME_TO_ACCEPT);
        contextManager.registerRequestContext(new GuildInviteRequest(languageConfig, time, requester, requested));
    }

    public @NotNull Optional<GuildInviteRequest> getRequest(@NotNull final Player player) {
        return Optional.ofNullable((GuildInviteRequest) contextManager.getRequestByRequested(player).orElse(null));
    }

    @Override
    public void cancel() {
        contextManager.cancel();
    }

}
