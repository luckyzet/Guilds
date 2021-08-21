package com.luckyzz.guilds.guild;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GuildMemberImpl implements GuildMember {

    private final String identifier;
    private GuildRank rank;

    GuildMemberImpl(@NotNull final String identifier, @NotNull final GuildRank rank) {
        this.identifier = identifier;
        this.rank = rank;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull GuildRank getRank() {
        return rank;
    }

    @Override
    public void updateRank(@NotNull final GuildRank rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildMemberImpl)) return false;
        final GuildMemberImpl that = (GuildMemberImpl) o;
        return new EqualsBuilder()
                .append(identifier, that.identifier)
                .append(rank, that.rank)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(rank)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "GuildMemberImpl{" +
                "identifier='" + identifier + '\'' +
                ", rank=" + rank +
                '}';
    }

}
