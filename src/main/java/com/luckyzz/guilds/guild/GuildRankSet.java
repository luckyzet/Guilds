package com.luckyzz.guilds.guild;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

public class GuildRankSet {

    private final Set<GuildRank> ranks;

    GuildRankSet(@NotNull final Set<GuildRank> ranks) {
        this.ranks = ranks;
    }

    public @NotNull GuildRank get(final int index) {
        return ranks.stream().filter(rank -> rank.getIndex() == index).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public void forEach(@NotNull final Consumer<GuildRank> consumer) {
        ranks.forEach(consumer);
    }

}
