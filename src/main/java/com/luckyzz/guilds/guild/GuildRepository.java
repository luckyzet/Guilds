package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.guild.cooldown.LeaveCooldown;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface GuildRepository extends AutoCloseable {

    @NotNull List<LeaveCooldown> loadCooldowns();

    void createCooldown(@NotNull final LeaveCooldown cooldown);

    void updateCooldown(@NotNull final LeaveCooldown cooldown);

    void removeCooldown(@NotNull final LeaveCooldown cooldown);

    @NotNull GuildSet loadGuilds(@NotNull final OptionConfig optionConfig);

    void createGuild(@NotNull final Guild guild);

    void registerMember(@NotNull final Guild guild, @NotNull final GuildMember member);

    void unregisterMember(@NotNull final Guild guild, @NotNull final GuildMember member);

    void updateMemberRank(@NotNull final Guild guild, @NotNull final GuildMember member);

    //

    void updateConditions(@NotNull final Guild guild, @NotNull final ExecutingLevelCondition<?> condition);

    void updateLevel(@NotNull final Guild guild);

    //

    void updateItems(@NotNull final Guild guild, @NotNull final String string, @NotNull final Set<GuildItem> items,
                     @NotNull final Set<GuildItem> newItems);

    void updateRank(@NotNull final Guild guild, @NotNull final GuildRank rank);

}
