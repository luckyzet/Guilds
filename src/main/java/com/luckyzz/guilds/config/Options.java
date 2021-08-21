package com.luckyzz.guilds.config;

import com.akamecoder.api.Pathable;
import org.jetbrains.annotations.NotNull;

public enum Options implements Pathable {

    DATABASE_TYPE("database.type"),
    DATABASE_HOSTNAME("database.hostname"),
    DATABASE_PORT("database.port"),
    DATABASE_NAME("database.database"),
    DATABASE_USERNAME("database.username"),
    DATABASE_PASSWORD("database.password"),
    TIME_TO_ACCEPT("settings.timeToAccept"),
    TIME_COOLDOWN("settings.cooldown");

    private final String path;

    Options(@NotNull final String path) {
        this.path = path;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }

    @Override
    public @NotNull String toString() {
        return "Options{" +
                "path='" + path + '\'' +
                "} " + super.toString();
    }

}
