package com.luckyzz.guilds.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class ItemDataImpl implements ItemData {

    private final ItemDataType type;
    private final Material material;
    private final String displayName;
    private final List<String> lore;

    ItemDataImpl(@NotNull final ItemDataType type, @Nullable final Material material, @Nullable final String displayName,
                 @Nullable final List<String> lore) {
        this.type = type;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    @Override
    public @NotNull ItemDataType getType() {
        return type;
    }

    @Override
    public @Nullable Material getMaterial() {
        return material;
    }

    @Override
    public @Nullable String getDisplayName() {
        return displayName;
    }

    @Override
    public @Nullable List<String> getLore() {
        return lore;
    }

    @Override
    public @NotNull ItemStack apply(@NotNull final ItemStack stack) {
        if (material != null) {
            stack.setType(material);
        }
        final ItemMeta meta = stack.getItemMeta();
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public @NotNull ItemStack get() {
        Material stackMaterial = material;
        if (stackMaterial == null) {
            stackMaterial = Material.AIR;
        }
        final ItemStack stack = new ItemStack(stackMaterial);
        final ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return stack;
        }
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDataImpl)) return false;
        final ItemDataImpl that = (ItemDataImpl) o;
        return new EqualsBuilder()
                .append(type, that.type)
                .append(material, that.material)
                .append(displayName, that.displayName)
                .append(lore, that.lore)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(material)
                .append(displayName)
                .append(lore)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "ItemDataImpl{" +
                "type" + type +
                ", material=" + material +
                ", displayName='" + displayName + '\'' +
                ", lore=" + lore +
                '}';
    }

}
