package com.luckyzz.guilds.guild;

import com.akamecoder.color.ColorUtils;
import com.akamecoder.util.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public enum MemberRank {

    FIRST(1, "[1]", MemberPossibility.CHAT, MemberPossibility.UPGRADE, MemberPossibility.BUY_SHOP, MemberPossibility.LEAVE),
    SECOND(FIRST, 2, "[2]", MemberPossibility.QUESTS),
    THIRD(SECOND, 3, "[3]", MemberPossibility.BANK),
    FOURTH(THIRD, 4, "[4]", MemberPossibility.INVITE, MemberPossibility.KICK),
    FIFTH(5, "[5]", MemberPossibility.ALL);

    private final int index;
    private final List<MemberPossibility> possibilities;
    private final String prefix;

    MemberRank(final int index, @NotNull final String prefix, @NotNull final MemberPossibility... possibilities) {
        this.index = index;
        this.possibilities = Arrays.asList(possibilities);
        this.prefix = ColorUtils.color(prefix);
    }

    MemberRank(@NotNull final MemberRank rank, final int index, @NotNull final String prefix, @NotNull final MemberPossibility... possibilities) {
        this.index = index;
        final List<MemberPossibility> newPossibilities = Lists.toList(rank.possibilities);
        newPossibilities.addAll(Arrays.asList(possibilities));
        this.possibilities = newPossibilities;
        this.prefix = ColorUtils.color(prefix);
    }

    public static @NotNull MemberRank fromIndex(final int index) {
        return Arrays.stream(values()).filter(rank -> rank.getIndex() == index).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public int getIndex() {
        return index;
    }

    public @NotNull String getPrefix() {
        return prefix;
    }

    public boolean isPossibility(@NotNull final MemberPossibility possibility) {
        return possibilities.contains(MemberPossibility.ALL) || possibilities.contains(possibility);
    }

}
