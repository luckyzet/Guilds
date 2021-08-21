package com.luckyzz.guilds.guild;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GuildItemList {

    private final List<GuildItem> items;

    GuildItemList(@NotNull final List<GuildItem> items) {
        this.items = items;
    }

    public @NotNull Stream<GuildItem> stream() {
        return items.stream();
    }

    public void registerItems(@NotNull final Set<GuildItem> items) {
        this.items.addAll(items);
    }

    public void unregisterItems(@NotNull final Set<GuildItem> items) {
        this.items.removeAll(items);
    }

    @Override
    public @NotNull String toString() {
        return "GuildItemList{" +
                "items=" + items +
                '}';
    }

}
