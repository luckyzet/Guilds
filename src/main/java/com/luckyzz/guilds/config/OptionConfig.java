package com.luckyzz.guilds.config;

import com.akamecoder.config.SettingConfig;
import com.akamecoder.guilds.config.level.*;
import com.akamecoder.itemstack.DefaultItemBuilder;
import com.akamecoder.itemstack.ItemBuilders;
import com.akamecoder.stoneofreturn.NravStoneOfReturnPlugin;
import com.akamecoder.stoneofreturn.stone.ReturnStoneApi;
import com.luckyzz.guilds.config.level.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.akamecoder.color.ColorUtils.color;

public class OptionConfig extends SettingConfig<Options> {

    private ConfigLevelSet levelSet;
    private HologramLineSet lineSet;
    private MenuDataSet menuDataSet;
    private ItemDataSet itemDataSet;
    private EntityHeadSet headSet;
    private final ReturnStoneApi stoneApi;

    public OptionConfig(@NotNull final Plugin plugin) {
        stoneApi = Bukkit.getServicesManager().getRegistration(ReturnStoneApi.class).getProvider();
        this.setup(config -> config.plugin(plugin).name("config").loader(settingConfig -> {
            final Set<ConfigLevel> levels = new HashSet<>();
            final Map<ConfigLevel, ConfigurationSection> map = new HashMap<>();
            final Configuration yaml = configuration();

            yaml.getConfigurationSection("levels").getKeys(false).forEach(levelKey -> {
                final ConfigurationSection levelSection = yaml.getConfigurationSection("levels." + levelKey);

                final int index = Integer.parseInt(levelKey);
                final LevelType levelType = LevelType.fromString(levelSection.getString("type"));

                final Set<ConfigLevelCondition<?>> conditions = new HashSet<>();
                final ConfigurationSection conditionSection = levelSection.getConfigurationSection("conditions");
                if (conditionSection != null) {
                    conditionSection.getKeys(false).forEach(conditionKey -> {
                        final ConfigurationSection section = levelSection.getConfigurationSection("conditions." + conditionKey);

                        final int conditionIndex = Integer.parseInt(conditionKey);
                        final ConditionType type = ConditionType.fromString(section.getString("type"));
                        final int amount = section.getInt("amount");
                        if (type == ConditionType.ITEMS) {
                            final Material material = Material.getMaterial(section.getString("material"));
                            conditions.add(new ItemLevelCondition(conditionIndex, material, amount));
                            return;
                        }
                        if (type == ConditionType.MOBS) {
                            final EntityType entity = EntityType.valueOf(section.getString("entity"));
                            conditions.add(new EntityLevelCondition(conditionIndex, entity, amount));
                        }
                    });
                }

                final Set<ConfigLevelFeature> features = new HashSet<>();
                final ConfigurationSection featureSection = levelSection.getConfigurationSection("features");
                if (featureSection != null) {
                    featureSection.getKeys(false).forEach(featureKey -> {
                        final ConfigurationSection section = levelSection.getConfigurationSection("features." + featureKey);

                        final FeatureType type = FeatureType.fromString(section.getString("type"));
                        final int amount = section.getInt("amount");

                        if (type == FeatureType.STORE_ITEM) {
                            final int indexNeed = section.getInt("index");
                            final ItemStack stack = loadStack(section.getConfigurationSection("stack"));
                            final CurrencyType currency = CurrencyType.fromString(section.getString("currency"));
                            final double money = section.getDouble("money");
                            features.add(new StoreLevelFeature(indexNeed, currency, type, stack, amount, money));
                            return;
                        }
                        if (type == FeatureType.EXTRA_BANK_PAGES) {
                            features.add(new AmountLevelFeature(type, amount));
                            return;
                        }
                        features.add(new DefaultLevelFuture(type));
                    });
                    final ConfigLevel level = new ConfigLevelImpl(index, levelType, conditions, features);
                    map.put(level, levelSection);
                    levels.add(level);
                }
            });

            map.forEach((level, section) -> {
                if (level instanceof ConfigLevelImpl) {
                    if (level.getType() == LevelType.LAST) {
                        return;
                    }
                    final ConfigLevelImpl levelImpl = (ConfigLevelImpl) level;
                    final int nextInt = section.getInt("next");

                    final ConfigLevel next = levels.stream().filter(got -> got.getIndex() == nextInt)
                            .findFirst().orElseThrow(IllegalArgumentException::new);
                    levelImpl.nextConfigLevel(next);
                }
            });

            levelSet = new ConfigLevelSet(levels);

            final Set<ItemData> itemData = new HashSet<>();
            yaml.getConfigurationSection("items").getKeys(false).forEach(typeString -> {
                final ConfigurationSection section = yaml.getConfigurationSection("items." + typeString);
                final ItemDataType type = ItemDataType.fromString(typeString);
                final Material material = Material.getMaterial(section.getString("material"));
                final String displayName = section.getString("displayName");
                final List<String> lore = section.getStringList("lore");

                final String displayFinal = displayName == null ? null : color(displayName);
                final List<String> loreFinal = lore == null ? null : (List<String>) color(lore);
                itemData.add(new ItemDataImpl(type, material, displayFinal, loreFinal));
            });
            itemDataSet = new ItemDataSet(itemData);

            final Set<HologramLine> lines = new HashSet<>();
            yaml.getConfigurationSection("hologram").getKeys(false).forEach(indexString -> {
                final ConfigurationSection section = yaml.getConfigurationSection("hologram." + indexString);
                final int index = Integer.parseInt(indexString);
                final HologramLineType type = HologramLineType.fromString(section.getString("type"));
                final String text = color(section.getString("text"));
                lines.add(new HologramLineImpl(index, type, text));
            });
            lineSet = new HologramLineSet(lines);

            final Set<MenuData> menuData = new HashSet<>();
            yaml.getConfigurationSection("menu").getKeys(false).forEach(typeString -> {
                final MenuDataType type = MenuDataType.fromString(typeString);
                final String title = color(yaml.getString("menu." + typeString + ".title"));
                menuData.add(new MenuDataImpl(type, title));
            });
            menuDataSet = new MenuDataSet(menuData);

            final Set<EntityHead> heads = new HashSet<>();
            yaml.getConfigurationSection("heads").getKeys(false).forEach(headKey -> {
                final ConfigurationSection section = yaml.getConfigurationSection("heads." + headKey);
                final EntityType type = EntityType.valueOf(headKey);
                final String displayName = section.getString("displayName");
                final Material material = Material.getMaterial(section.getString("material"));
                final String skull = section.getString("skull");

                final String displayFinal = displayName == null ? null : color(displayName);
                heads.add(new EntityHeadImpl(type, displayFinal, material, skull));
            });
            headSet = new EntityHeadSet(heads);
        }));
    }

    private @NotNull ItemStack loadStack(@NotNull final ConfigurationSection section) {
        final Material material = Material.getMaterial(section.getString("material"));
        if(material == null && section.getString("material").equals("STONE_OF_RETURN")) {
            return stoneApi.createReturnStoneStack();
        }
        final int amount = section.getInt("amount", 1);
        final String displayName = section.getString("displayName");
        final List<String> lore = section.getStringList("lore");
        final Map<Enchantment, Integer> enchantments = new HashMap<>();
        final ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
        if (enchantmentSection != null) {
            enchantmentSection.getKeys(false).forEach(enchantmentKey -> {
                final Enchantment enchantment = Enchantment.getByName(enchantmentKey);
                final int level = enchantmentSection.getInt(enchantmentKey);
                enchantments.put(enchantment, level);
            });
        }

        final DefaultItemBuilder builder = ItemBuilders.defaultBuilder()
                .material(material)
                .amount(amount);
        if (displayName != null) {
            builder.displayName(color(displayName));
        }
        if (lore != null) {
            builder.lore(color(lore));
        }
        return builder.enchantments(enchantments).create();
    }

    public @NotNull ConfigLevelSet getLevelSet() {
        return levelSet;
    }

    public @NotNull HologramLineSet getLineSet() {
        return lineSet;
    }

    public @NotNull MenuDataSet getMenuDataSet() {
        return menuDataSet;
    }

    public @NotNull ItemDataSet getItemDataSet() {
        return itemDataSet;
    }

    public @NotNull EntityHeadSet getHeadSet() {
        return headSet;
    }

}
