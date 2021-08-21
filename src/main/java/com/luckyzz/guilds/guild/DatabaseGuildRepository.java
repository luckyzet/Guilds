package com.luckyzz.guilds.guild;

import com.akamecoder.database.Database;
import com.akamecoder.database.DatabaseType;
import com.akamecoder.database.HikariDatabase;
import com.akamecoder.database.execute.DatabaseExecutor;
import com.akamecoder.database.execute.ExecutionType;
import com.akamecoder.date.DateFormat;
import com.akamecoder.date.DateParseFormat;
import com.akamecoder.guilds.config.*;
import com.luckyzz.guilds.config.*;
import com.luckyzz.guilds.config.level.ConditionType;
import com.luckyzz.guilds.config.level.ConfigLevel;
import com.luckyzz.guilds.config.level.FeatureType;
import com.luckyzz.guilds.guild.cooldown.LeaveCooldown;
import com.akamecoder.position.PositionSerialize;
import com.akamecoder.serialize.SerializeType;
import com.akamecoder.serialize.SerializeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DatabaseGuildRepository implements GuildRepository {

    private final HikariDatabase database;
    private final DatabaseExecutor executor;
    private final Plugin plugin;

    DatabaseGuildRepository(@NotNull final OptionConfig optionConfig, @NotNull final Plugin plugin) {
        this.plugin = plugin;
        this.database = Database.createDatabase(builder -> builder.hikari()
                .plugin(plugin)
                .type(DatabaseType.fromString(optionConfig.getOption(Options.DATABASE_TYPE)))
                .file(new File(plugin.getDataFolder(), "database.db"))
                .hostname(optionConfig.getOption(Options.DATABASE_HOSTNAME))
                .port((int) optionConfig.getOption(Options.DATABASE_PORT))
                .database(optionConfig.getOption(Options.DATABASE_NAME))
                .username(optionConfig.getOption(Options.DATABASE_USERNAME))
                .password(optionConfig.getOption(Options.DATABASE_PASSWORD))
        );
        this.executor = database.executor();
    }

    @Override
    public @NotNull List<LeaveCooldown> loadCooldowns() {
        final List<LeaveCooldown> cooldowns = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `cooldowns` (`owner` VARCHAR(255) NOT NULL, `time` VARCHAR(255) NOT NULL);");

            executor.result(ExecutionType.SYNC, "SELECT * FROM cooldowns;", result -> {
                while (result.next()) {
                    cooldowns.add(new LeaveCooldownImpl(result.getString("owner"), result.getInt("time")));
                }
            });
        });
        return cooldowns;
    }

    @Override
    public void createCooldown(@NotNull final LeaveCooldown cooldown) {
        executor.update("INSERT INTO cooldowns VALUES (?, ?);", cooldown.getIdentifier(), cooldown.getTimeLeft());
    }

    @Override
    public void updateCooldown(@NotNull final LeaveCooldown cooldown) {
        executor.update("UPDATE cooldowns SET `time` = ? WHERE `owner` = ?;", cooldown.getTimeLeft(), cooldown.getIdentifier());
    }

    @Override
    public void removeCooldown(@NotNull final LeaveCooldown cooldown) {
        executor.update("DELETE FROM cooldowns WHERE owner = ?;", cooldown.getIdentifier());
    }

    @Override
    public @NotNull GuildSet loadGuilds(@NotNull final OptionConfig optionConfig) {
        final Set<Guild> guilds = new HashSet<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `guilds` (`identifier` VARCHAR(255) NOT NULL, `date` VARCHAR(255) NOT NULL, " +
                    "`location` VARCHAR(10000) NOT NULL, `level` VARCHAR(255) NOT NULL);");
            executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `conditions` (`guild` VARCHAR(255) NOT NULL, `condition` VARCHAR(255) NOT NULL, " +
                    "`index` VARCHAR(255) NOT NULL, `type` VARCHAR(255) NOT NULL, `need` VARCHAR(255) NOT NULL, `amount` VARCHAR(255) NOT NULL);");
            executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `members` (`guild` VARCHAR(255) NOT NULL, `name` VARCHAR(255) NOT NULL, " +
                    "`rank` VARCHAR(255) NOT NULL);");
            executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `items` (`guild` VARCHAR(255) NOT NULL, `type` VARCHAR(255) NOT NULL, " +
                    "`index` VARCHAR(255) NOT NULL, `stack` VARCHAR(10000) NOT NULL);");
            executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `ranks` (`guild` VARCHAR(255) NOT NULL, `index` VARCHAR(255) NOT NULL, " +
                    "`prefix` VARCHAR(255) NOT NULL);");

            executor.result(ExecutionType.SYNC, "SELECT * FROM guilds;", guildResult -> {
                while (guildResult.next()) {
                    final String identifier = guildResult.getString("identifier");
                    final LocalDate date = DateParseFormat.parseDate(guildResult.getString("date"), DateFormat.DATE);
                    final Location location = PositionSerialize.string().serialize(guildResult.getString("location"),
                            SerializeType.BASE64);
                    final int level = guildResult.getInt("level");

                    final Set<ExecutingLevelCondition<?>> conditions = new HashSet<>();
                    executor.result(ExecutionType.SYNC, "SELECT * FROM conditions WHERE guild = ?;", result -> {
                        while (result.next()) {
                            final ConditionType conditionType = ConditionType.fromString(result.getString("condition"));
                            final int index = result.getInt("index");
                            final int need = result.getInt("need");
                            final int amount = result.getInt("amount");
                            if (conditionType == ConditionType.ITEMS) {
                                final Material material = Material.getMaterial(result.getString("type"));
                                conditions.add(new ItemExecutingCondition(index, material, need, amount));
                                continue;
                            }
                            final EntityType type = EntityType.valueOf(result.getString("type"));
                            conditions.add(new EntityExecutingCondition(index, type, need, amount));
                        }
                    }, identifier);
                    if(conditions.isEmpty() && optionConfig.getLevelSet().getLevel(level + 1).isPresent()) {
                        optionConfig.getLevelSet().getLevel(level + 1).get().getConditionSet().conditions().forEach(condition -> {
                            final ConditionType conditionType = condition.getConditionType();
                            final int index = condition.getIndex();
                            final int need = condition.getNeed();
                            executor.update(ExecutionType.SYNC, "INSERT INTO conditions VALUES (?, ?, ?, ?, ?, ?);", identifier,
                                    condition.getConditionType().name(), condition.getIndex(), condition.getType().toString(),
                                    condition.getNeed(), 0);
                            if (conditionType == ConditionType.ITEMS) {
                                final Material material = Material.getMaterial(((ItemLevelCondition) condition).getType().name());
                                conditions.add(new ItemExecutingCondition(index, material, need, 0));
                                return;
                            }
                            final EntityType type = EntityType.valueOf(((EntityLevelCondition) condition).getType().name());
                            conditions.add(new EntityExecutingCondition(index, type, need, 0));
                        });
                    }

                    final Set<GuildRank> ranks = new HashSet<>();
                    executor.result(ExecutionType.SYNC, "SELECT * FROM ranks WHERE guild = ?;", result -> {
                        while (result.next()) {
                            final MemberRank rank = MemberRank.fromIndex(result.getInt("index"));
                            final String prefix = result.getString("prefix");
                            ranks.add(new GuildRank(rank, prefix));
                        }
                    }, identifier);

                    final List<GuildMember> members = new ArrayList<>();
                    executor.result(ExecutionType.SYNC, "SELECT * FROM members WHERE guild = ?;", result -> {
                        while (result.next()) {
                            final String name = result.getString("name");
                            final int index = result.getInt("rank");
                            final GuildRank rank = ranks.stream().filter(got -> got.getIndex() == index).findFirst()
                                    .orElseThrow(IllegalArgumentException::new);
                            members.add(new GuildMemberImpl(name, rank));
                        }
                    }, identifier);

                    final List<GuildStoreItem> store = new ArrayList<>();
                    ConfigLevel nowLevel = optionConfig.getLevelSet().getDefaultLevel().orElseThrow(IllegalArgumentException::new);
                    while (nowLevel.getIndex() <= level) {
                        nowLevel.getFeatureSet().stream()
                                .filter(feature -> feature.getType() == FeatureType.STORE_ITEM)
                                .map(feature -> (StoreLevelFeature) feature).forEach(feature ->
                                    store.add(new GuildStoreItemImpl(feature.getCurrency(), feature.getIndex(),
                                        feature.getStack(), feature.getMoney())
                                ));
                        nowLevel = optionConfig.getLevelSet().getLevel(nowLevel.getIndex() + 1).orElse(null);
                        if(nowLevel == null) {
                            break;
                        }
                    }

                    final List<GuildItem> bank = new ArrayList<>(), trophies = new ArrayList<>();
                    executor.result(ExecutionType.SYNC, "SELECT * FROM items WHERE guild = ?;", result -> {
                        while (result.next()) {
                            final String type = result.getString("type");
                            final int index = result.getInt("index");
                            final ItemStack stack = SerializeUtil.decode(result.getString("stack"));

                            final GuildItem item = new GuildItemImpl(index, stack);
                            if (type.equals("bank")) {
                                bank.add(item);
                                continue;
                            }
                            trophies.add(item);
                        }
                    }, identifier);

                    final ConfigLevel cfgLevel = optionConfig.getLevelSet().getLevel(level).orElseThrow(IllegalArgumentException::new);
                    Bukkit.getScheduler().runTask(plugin, () ->
                            guilds.add(new GuildImpl(optionConfig, identifier, date, location, members, bank, trophies, store, ranks,
                                    new GuildLevelImpl(cfgLevel, conditions))));
                }
            });
        });
        return new GuildSet(guilds);
    }

    @Override
    public void createGuild(@NotNull final Guild guild) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final GuildLevel level = guild.getLevel();
            final String identifier = guild.getIdentifier();
            final String date = DateParseFormat.formatDate(guild.getDate(), DateFormat.DATE);
            executor.update(ExecutionType.SYNC, "INSERT INTO guilds VALUES (?, ?, ?, ?);", identifier, date,
                    PositionSerialize.string().serialize(guild.getPoint().getLocation(), SerializeType.BASE64), level.getIndex());
            level.getConditionSet().forEach(condition -> executor.update(ExecutionType.SYNC, "INSERT INTO conditions VALUES (?, ?, ?, ?, ?, ?);",
                    identifier, condition.getConditionType().name(), condition.getIndex(), condition.getType().toString(),
                    condition.getNeed(), condition.getAmount()));
            final GuildMember owner = guild.getMemberList().getOwner();
            executor.update(ExecutionType.SYNC, "INSERT INTO members VALUES (?, ?, ?);", identifier, owner.getIdentifier(), owner.getRank().getIndex());
            guild.getRankSet().forEach(rank -> executor.update(ExecutionType.SYNC, "INSERT INTO ranks VALUES (?, ?, ?);",
                    identifier, rank.getIndex(), rank.getPrefix()));
        });
    }

    @Override
    public void registerMember(@NotNull final Guild guild, @NotNull final GuildMember member) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> executor.update(ExecutionType.SYNC, "INSERT INTO members VALUES (?, ?, ?);",
                guild.getIdentifier(), member.getIdentifier(), member.getRank().getRank().getIndex()));
    }

    @Override
    public void unregisterMember(@NotNull final Guild guild, @NotNull final GuildMember member) {
        executor.update("DELETE FROM members WHERE guild = ? AND name = ?;", guild.getIdentifier(), member.getIdentifier());
    }

    @Override
    public void updateMemberRank(@NotNull final Guild guild, @NotNull final GuildMember member) {
        executor.update("UPDATE members SET `rank` = ? WHERE `guild` = ? AND `name` = ?;", member.getRank().getIndex(),
                guild.getIdentifier(), member.getIdentifier());
    }

    @Override
    public void updateConditions(@NotNull final Guild guild, @NotNull final ExecutingLevelCondition<?> condition) {
        executor.update("UPDATE conditions SET `amount` = ? WHERE `guild` = ? AND `index` = ?;", condition.getAmount(),
                guild.getIdentifier(), condition.getIndex());
    }

    @Override
    public void updateLevel(@NotNull final Guild guild) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String identifier = guild.getIdentifier();
            final GuildLevel level = guild.getLevel();

            executor.update(ExecutionType.SYNC, "UPDATE guilds SET `level` = ? WHERE `identifier` = ?;",
                    level.getIndex(), identifier);
            executor.update(ExecutionType.SYNC, "DELETE FROM conditions WHERE guild = ?;", identifier);
            level.getConditionSet().forEach(condition -> {
                executor.update(ExecutionType.SYNC, "INSERT INTO conditions VALUES (?, ?, ?, ?, ?, ?);", identifier,
                        condition.getConditionType().name(), condition.getIndex(), condition.getType().toString(),
                        condition.getNeed(), condition.getAmount());
            });
        });
    }

    @Override
    public void updateItems(@NotNull final Guild guild, @NotNull final String string, @NotNull final Set<GuildItem> items, @NotNull final Set<GuildItem> newItems) {
        items.forEach(item -> executor.update(ExecutionType.SYNC, "DELETE FROM items WHERE `guild` = ? AND `type` = ? AND `index` = ?;",
                guild.getIdentifier(), string, item.getIndex()));
        newItems.forEach(item -> executor.update(ExecutionType.SYNC, "INSERT INTO items VALUES (?, ?, ?, ?);",
                guild.getIdentifier(), string, item.getIndex(), SerializeUtil.encode(item.getStack())));
    }

    @Override
    public void updateRank(@NotNull final Guild guild, @NotNull final GuildRank rank) {
        executor.update(ExecutionType.SYNC, "UPDATE ranks SET `prefix` = ? WHERE `guild` = ? AND `index` = ?;",
                rank.getPrefix(), guild.getIdentifier(), rank.getIndex());
    }

    @Override
    public void close() throws Exception {
        database.close();
    }

}
