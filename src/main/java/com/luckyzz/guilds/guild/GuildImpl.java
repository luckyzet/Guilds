package com.luckyzz.guilds.guild;

import com.luckyzz.guilds.config.EntityLevelCondition;
import com.luckyzz.guilds.config.ItemLevelCondition;
import com.luckyzz.guilds.config.OptionConfig;
import com.luckyzz.guilds.config.StoreLevelFeature;
import com.luckyzz.guilds.config.level.ConditionType;
import com.luckyzz.guilds.config.level.ConfigLevel;
import com.luckyzz.guilds.config.level.ConfigLevelCondition;
import com.luckyzz.guilds.config.level.FeatureType;
import com.akamecoder.message.Message;
import com.akamecoder.util.Lists;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class GuildImpl implements Guild {

    private final String identifier;
    private final LocalDate date;
    private final GuildPoint point;
    private final GuildMemberList memberList;
    private final GuildItemList bankList;
    private final GuildItemList trophiesList;
    private final GuildStoreItemList storeList;
    private final GuildMenuSet menuSet;
    private final GuildRankSet rankSet;
    private GuildLevel level;

    GuildImpl(@NotNull final OptionConfig optionConfig, @NotNull final String identifier, @NotNull final LocalDate date,
              @NotNull final Location location, @NotNull final List<GuildMember> members, @NotNull final List<GuildItem> bank,
              @NotNull final List<GuildItem> trophies, @NotNull final List<GuildStoreItem> store, @NotNull final Set<GuildRank> ranks,
              @NotNull final GuildLevel level) {
        this.identifier = identifier;
        this.date = date;
        this.memberList = new GuildMemberList(members);
        this.bankList = new GuildItemList(bank);
        this.trophiesList = new GuildItemList(trophies);
        this.storeList = new GuildStoreItemList(store);
        this.menuSet = new GuildMenuSet();
        this.rankSet = new GuildRankSet(ranks);

        this.level = level;

        location.getWorld().getNearbyEntities(location.clone().add(0, 1, 0), 3, 3, 3).stream()
                .filter(entity -> entity.getType() == EntityType.ARMOR_STAND).forEach(Entity::remove);
        this.point = new GuildPointImpl(optionConfig, this, location);
    }

    GuildImpl(@NotNull final GuildManager guildManager, @NotNull final OptionConfig optionConfig, @NotNull final String identifier,
              @NotNull final Location location, @NotNull final String owner, @NotNull final ConfigLevel level,
              @NotNull final Set<GuildRank> ranks) {
        this.identifier = identifier;
        this.date = LocalDate.now();
        this.rankSet = new GuildRankSet(ranks);
        this.memberList = new GuildMemberList(Lists.of(new GuildMemberImpl(owner, rankSet.get(5))));
        this.bankList = new GuildItemList(new ArrayList<>());
        this.trophiesList = new GuildItemList(new ArrayList<>());
        this.storeList = new GuildStoreItemList(new ArrayList<>());
        this.menuSet = new GuildMenuSet();

        this.updateLevel(guildManager, level);
        this.point = new GuildPointImpl(optionConfig, this, location);
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull LocalDate getDate() {
        return date;
    }

    @Override
    public @NotNull GuildPoint getPoint() {
        return point;
    }

    @Override
    public @NotNull GuildLevel getLevel() {
        return level;
    }

    @Override
    public void updateLevel(@NotNull final GuildManager guildManager, @NotNull final ConfigLevel level) {
        final Set<ExecutingLevelCondition<?>> newConditions = new HashSet<>();
        level.getNextConfigLevel().ifPresent(next -> {
            for (final ConfigLevelCondition<?> condition : next.getConditionSet().conditions()) {
                final ConditionType type = condition.getConditionType();
                if (type == ConditionType.ITEMS) {
                    final ItemLevelCondition levelCondition = (ItemLevelCondition) condition;
                    newConditions.add(new ItemExecutingCondition(levelCondition.getIndex(), levelCondition.getType(),
                            levelCondition.getNeed()));
                    continue;
                }
                final EntityLevelCondition levelCondition = (EntityLevelCondition) condition;
                newConditions.add(new EntityExecutingCondition(levelCondition.getIndex(), levelCondition.getType(),
                        levelCondition.getNeed()));
            }
        });
        for (final StoreLevelFeature feature : level.getFeatureSet().stream()
                .filter(feature -> feature.getType() == FeatureType.STORE_ITEM)
                .map(feature -> (StoreLevelFeature) feature)
                .collect(Collectors.toSet())) {
            guildManager.registerStoreItem(this, new GuildStoreItemImpl(feature.getCurrency(), feature.getIndex(),
                    feature.getStack(), feature.getMoney()));
        }
        this.level = new GuildLevelImpl(level, newConditions);
    }

    @Override
    public @NotNull GuildMemberList getMemberList() {
        return memberList;
    }

    @Override
    public @NotNull GuildItemList getBankList() {
        return bankList;
    }

    @Override
    public @NotNull GuildItemList getTrophiesList() {
        return trophiesList;
    }

    @Override
    public @NotNull GuildStoreItemList getStoreList() {
        return storeList;
    }

    @Override
    public @NotNull GuildMenuSet getMenuSet() {
        return menuSet;
    }

    @Override
    public @NotNull GuildRankSet getRankSet() {
        return rankSet;
    }

    @Override
    public void broadcast(@NotNull final Message message) {
        memberList.forEach(member -> {
            final Player player = Bukkit.getPlayerExact(member.getIdentifier());
            if (player != null) {
                message.send(player);
            }
        });
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildImpl)) return false;
        final GuildImpl that = (GuildImpl) o;
        return new EqualsBuilder()
                .append(identifier, that.identifier)
                .append(date, that.date)
                .append(point, that.point)
                .append(memberList, that.memberList)
                .append(bankList, that.bankList)
                .append(trophiesList, that.trophiesList)
                .append(storeList, that.storeList)
                .append(menuSet, that.menuSet)
                .append(rankSet, that.rankSet)
                .append(level, that.level)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(date)
                .append(point)
                .append(memberList)
                .append(bankList)
                .append(trophiesList)
                .append(storeList)
                .append(menuSet)
                .append(rankSet)
                .append(level)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "GuildImpl{" +
                "identifier='" + identifier + '\'' +
                ", date=" + date +
                ", point=" + point +
                ", memberList=" + memberList +
                ", bankList=" + bankList +
                ", trophiesList=" + trophiesList +
                ", storeList=" + storeList +
                ", menuSet=" + menuSet +
                ", rankSet=" + rankSet +
                ", level=" + level +
                '}';
    }

}
