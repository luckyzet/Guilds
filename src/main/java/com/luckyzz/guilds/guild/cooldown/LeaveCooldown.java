package com.luckyzz.guilds.guild.cooldown;

import com.akamecoder.api.Identifiable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface LeaveCooldown extends Identifiable<String> {

    default @NotNull Optional<Player> getOwner() {
        return Optional.ofNullable(Bukkit.getPlayer(getIdentifier()));
    }

    boolean subtractTime();

    int getTimeLeft();

}
