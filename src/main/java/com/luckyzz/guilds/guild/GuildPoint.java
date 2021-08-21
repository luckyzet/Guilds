package com.luckyzz.guilds.guild;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface GuildPoint {

    @NotNull Location getLocation();

    void updateHologramData(@NotNull final Guild guild);

    void remove();

}
