package com.luckyzz.guilds.config;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public class MenuDataSet {

    private final Set<MenuData> data;

    MenuDataSet(@NotNull final Set<MenuData> data) {
        this.data = data;
    }

    public @NotNull Stream<MenuData> stream() {
        return data.stream();
    }

    public @NotNull MenuData getData(@NotNull final MenuDataType type) {
        return stream().filter(data -> data.getType().equals(type)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public @NotNull String toString() {
        return "MenuDataSet{" +
                "data=" + data +
                '}';
    }

}
