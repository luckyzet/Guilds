package com.luckyzz.guilds.config;

import com.luckyzz.guilds.config.level.ConfigLevelFeature;
import com.luckyzz.guilds.config.level.FeatureType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConfigFeatureSet {

    private final Set<ConfigLevelFeature> features;

    ConfigFeatureSet(@NotNull final Set<ConfigLevelFeature> features) {
        this.features = features;
    }

    public @NotNull Stream<ConfigLevelFeature> stream() {
        return features.stream();
    }

    public void forEach(@NotNull final Consumer<ConfigLevelFeature> consumer) {
        features.forEach(consumer);
    }

    public @NotNull Set<ConfigLevelFeature> features() {
        return features;
    }

    public @NotNull Optional<ConfigLevelFeature> getFeature(@NotNull final FeatureType type) {
        return stream().filter(feature -> feature.getType() == type).findFirst();
    }

    @Override
    public @NotNull String toString() {
        return "ConfigFeatureSet{" +
                "features=" + features +
                '}';
    }

}
