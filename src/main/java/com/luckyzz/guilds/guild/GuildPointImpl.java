package com.luckyzz.guilds.guild;

import com.akamecoder.date.DateFormat;
import com.akamecoder.date.DateParseFormat;
import com.luckyzz.guilds.config.HologramLine;
import com.luckyzz.guilds.config.HologramLineType;
import com.luckyzz.guilds.config.OptionConfig;
import com.akamecoder.hologram.Hologram;
import com.akamecoder.hologram.HologramBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

class GuildPointImpl implements GuildPoint {

    private final OptionConfig optionConfig;
    private final Location location;
    private Hologram hologram;

    GuildPointImpl(@NotNull final OptionConfig optionConfig, @NotNull final Guild guild, @NotNull final Location location) {
        this.optionConfig = optionConfig;
        this.location = location;
        this.updateHologramData(guild);
        location.getBlock().setType(Material.CHEST);
    }

    private void line(@NotNull final HologramBuilder hologramBuilder, @NotNull final HologramLine hologramLine,
                      @NotNull final Function<String, String> function) {
        hologramBuilder.line(line -> line
                .index(hologramLine.getIndex())
                .text(function.apply(hologramLine.getText())));
    }

    @Override
    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public void updateHologramData(@NotNull final Guild guild) {
        if(hologram != null) {
            hologram.remove();
        }
        hologram = Hologram.createHologram(builder -> {
            builder.location(location.clone().add(0, 1, 0));
            optionConfig.getLineSet().stream().sorted(Comparator.comparingInt(HologramLine::getIndex)).forEach(configLine -> {
                final HologramLineType type = configLine.getType();
                if (type == HologramLineType.COUNT_MEMBERS) {
                    line(builder, configLine, text -> text.replace("%count%", String.valueOf(guild.getMemberList().getCount())));
                    return;
                }
                if (type == HologramLineType.GUILD_LEVEL) {
                    line(builder, configLine, text -> text.replace("%level%", String.valueOf(guild.getLevel().getIndex())));
                    return;
                }
                if (type == HologramLineType.GUILD_OWNER) {
                    line(builder, configLine, text -> text.replace("%owner%", guild.getMemberList().getOwner().getIdentifier()));
                    return;
                }
                if (type == HologramLineType.DATE) {
                    line(builder, configLine, text -> text.replace("%date%", DateParseFormat.formatDate(guild.getDate(), DateFormat.DATE)));
                    return;
                }
                if (type == HologramLineType.IDENTIFIER) {
                    line(builder, configLine, text -> text.replace("%identifier%", guild.getIdentifier()));
                }
            });
        });
    }

    @Override
    public void remove() {
        hologram.remove();
        location.getWorld().getNearbyEntities(location.clone().add(0, 1, 0), 3, 3, 3).stream()
                .filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);
        location.getBlock().setType(Material.AIR);
    }

    @Override
    public @NotNull String toString() {
        return "GuildPointImpl{" +
                "optionConfig=" + optionConfig +
                ", location=" + location +
                ", hologram=" + hologram +
                '}';
    }

}
