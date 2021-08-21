package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.guild.cooldown.LeaveCooldown;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class EmptyGuildRepository implements GuildRepository {
    @Override
    public @NotNull List<LeaveCooldown> loadCooldowns() {
        return new ArrayList<>();
    }

    @Override
    public void createCooldown(@NotNull LeaveCooldown cooldown) {
    }

    @Override
    public void updateCooldown(@NotNull LeaveCooldown cooldown) {
    }

    @Override
    public void removeCooldown(@NotNull LeaveCooldown cooldown) {
    }

    @Override
    public @NotNull GuildSet loadGuilds(@NotNull OptionConfig optionConfig) {
        return new GuildSet(new HashSet<>());
    }

    @Override
    public void createGuild(@NotNull Guild guild) {
    }

    @Override
    public void registerMember(@NotNull Guild guild, @NotNull GuildMember member) {
    }

    @Override
    public void unregisterMember(@NotNull Guild guild, @NotNull GuildMember member) {
    }

    @Override
    public void updateMemberRank(@NotNull Guild guild, @NotNull GuildMember member) {
    }

    @Override
    public void updateConditions(@NotNull Guild guild, @NotNull ExecutingLevelCondition<?> condition) {
    }

    @Override
    public void updateLevel(@NotNull Guild guild) {
    }

    @Override
    public void updateItems(@NotNull Guild guild, @NotNull String string, @NotNull Set<GuildItem> items, @NotNull Set<GuildItem> newItems) {
    }

    @Override
    public void updateRank(@NotNull Guild guild, @NotNull GuildRank rank) {
    }

    @Override
    public void close() {
    }

}
