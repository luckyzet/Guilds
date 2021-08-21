package com.luckyzz.guilds.guild;

import com.akamecoder.util.Lists;

import java.util.List;

interface GuildStorageMenu {

    List<Integer> panes = Lists.of(7, 16, 17, 25);

    int next = 8;

    int prev = 26;

    static boolean isPaneNextPrev(final int slot) {
        return panes.contains(slot) || next == slot || prev == slot;
    }

}
