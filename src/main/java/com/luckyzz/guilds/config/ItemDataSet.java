package com.luckyzz.guilds.config;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public class ItemDataSet {

    private final Set<ItemData> data;

    ItemDataSet(@NotNull final Set<ItemData> data) {
        this.data = data;
    }

    public @NotNull Stream<ItemData> stream() {
        return data.stream();
    }

    public @NotNull ItemData getData(@NotNull final ItemDataType type) {
        return stream().filter(data -> data.getType() == type).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public @NotNull String toString() {
        return "ItemDataSet{" +
                "data=" + data +
                '}';
    }

}
