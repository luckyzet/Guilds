package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.guild.cooldown.LeaveCooldown;
import org.jetbrains.annotations.NotNull;

class LeaveCooldownImpl implements LeaveCooldown {

    private final String identifier;
    private int time;

    LeaveCooldownImpl(@NotNull final String identifier, final int time) {
        this.identifier = identifier;
        this.time = time;
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean subtractTime() {
        if (time > 10) {
            time -= 10;
            return true;
        }
        return false;
    }

    @Override
    public int getTimeLeft() {
        return time;
    }

    @Override
    public @NotNull String toString() {
        return "LeaveCooldownImpl{" +
                "identifier='" + identifier + '\'' +
                ", time=" + time +
                '}';
    }

}
