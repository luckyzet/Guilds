package com.luckyzz.guilds.guild;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GuildMemberList {

    private final List<GuildMember> members;

    GuildMemberList(@NotNull final List<GuildMember> members) {
        this.members = members;
    }

    public @NotNull Stream<GuildMember> stream() {
        return members.stream();
    }

    public void forEach(@NotNull final Consumer<GuildMember> consumer) {
        members.forEach(consumer);
    }

    public int getCount() {
        return members.size();
    }

    public @NotNull GuildMember getOwner() {
        return stream().filter(member -> member.getRank().getIndex() == 5).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public @NotNull Optional<GuildMember> getMember(@NotNull final String identifier) {
        return stream().filter(member -> member.getIdentifier().equals(identifier)).findFirst();
    }

    public @NotNull Optional<GuildMember> getMember(@NotNull final Player player) {
        return getMember(player.getName());
    }

    public void registerMember(@NotNull final GuildMember member) {
        members.add(member);
    }

    public void unregisterMember(@NotNull final GuildMember member) {
        members.remove(member);
    }

    @Override
    public @NotNull String toString() {
        return "GuildMemberList{" +
                "members=" + members +
                '}';
    }

}
