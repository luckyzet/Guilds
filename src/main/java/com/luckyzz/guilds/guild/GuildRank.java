package com.luckyzz.guilds.guild;

import com.akamecoder.util.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GuildRank {

    private final MemberRank rank;
    private String prefix;

    GuildRank(@NotNull final MemberRank rank, @NotNull final String prefix) {
        this.rank = rank;
        this.prefix = prefix;
    }

    GuildRank(@NotNull final MemberRank rank) {
        this(rank, rank.getPrefix());
    }

    public static @NotNull Set<GuildRank> defaultRanks() {
        return Sets.of(new GuildRank(MemberRank.FIRST),
                new GuildRank(MemberRank.SECOND),
                new GuildRank(MemberRank.THIRD),
                new GuildRank(MemberRank.FOURTH),
                new GuildRank(MemberRank.FIFTH));
    }

    public boolean isLess(@NotNull final GuildRank rank) {
        return getIndex() < rank.getIndex();
    }

    public @NotNull MemberRank getRank() {
        return rank;
    }

    public int getIndex() {
        return rank.getIndex();
    }

    public boolean isPossibility(@NotNull final MemberPossibility possibility) {
        return rank.isPossibility(possibility);
    }

    public @NotNull String getPrefix() {
        return prefix;
    }

    public void setPrefix(@NotNull final String prefix) {
        this.prefix = prefix;
    }

}
