package com.luckyzz.guilds.config;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HologramLineSet {

    private final Set<HologramLine> lines;

    HologramLineSet(@NotNull final Set<HologramLine> lines) {
        this.lines = lines;
    }

    public @NotNull Stream<HologramLine> stream() {
        return lines.stream();
    }

    public void forEach(@NotNull final Consumer<HologramLine> consumer) {
        lines.forEach(consumer);
    }

    @Override
    public @NotNull String toString() {
        return "HologramLineSet{" +
                "lines=" + lines +
                '}';
    }

}
