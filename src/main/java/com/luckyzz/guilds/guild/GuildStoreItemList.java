package com.luckyzz.guilds.guild;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GuildStoreItemList {

    private final List<GuildStoreItem> items;

    GuildStoreItemList(@NotNull final List<GuildStoreItem> items) {
        this.items = items;
    }

    public @NotNull Stream<GuildStoreItem> stream() {
        return items.stream();
    }

    public @NotNull Optional<GuildStoreItem> getItem(final int index) {
        return stream().filter(item -> item.getIndex() == index).findFirst();
    }

    public void registerStoreItem(@NotNull final GuildStoreItem item) {
        items.add(item);
    }

    public void unregisterStoreItem(@NotNull final GuildStoreItem item) {
        items.remove(item);
    }

    @Override
    public @NotNull String toString() {
        return "GuildStoreItemList{" +
                "items=" + items +
                '}';
    }

}
