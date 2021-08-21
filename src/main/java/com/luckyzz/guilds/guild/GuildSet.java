package com.luckyzz.guilds.guild;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GuildSet {

    private final Set<Guild> guilds;

    GuildSet(@NotNull final Set<Guild> guilds) {
        this.guilds = guilds;
    }

    public @NotNull Stream<Guild> stream() {
        return guilds.stream();
    }

    public void forEach(@NotNull final Consumer<Guild> consumer) {
        guilds.forEach(consumer);
    }

    public @NotNull Optional<Guild> getGuild(@NotNull final Location location) {
        return stream().filter(guild -> {
            final Location got = guild.getPoint().getLocation();
            return got.getWorld().getName().equals(location.getWorld().getName()) &&
                    got.getBlockX() == location.getBlockX() && got.getBlockY() == location.getBlockY() &&
                    got.getBlockZ() == location.getBlockZ();
        }).findFirst();
    }

    public @NotNull Optional<Guild> getGuild(@NotNull final Player player) {
        return stream().filter(guild -> guild.getMemberList().getMember(player).isPresent()).findFirst();
    }

    public void registerGuild(@NotNull final Guild guild) {
        guilds.add(guild);
    }

    @Override
    public @NotNull String toString() {
        return "GuildSet{" +
                "guilds=" + guilds +
                '}';
    }

}
