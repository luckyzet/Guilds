package com.luckyzz.guilds.guild;

import com.akamecoder.api.Identifiable;
import com.luckyzz.guilds.config.level.ConfigLevel;
import com.akamecoder.message.Message;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public interface Guild extends Identifiable<String> {

    @NotNull LocalDate getDate();

    @NotNull GuildPoint getPoint();

    @NotNull GuildLevel getLevel();

    void updateLevel(@NotNull final GuildManager guildManager, @NotNull final ConfigLevel level);

    @NotNull GuildMemberList getMemberList();

    @NotNull GuildItemList getBankList();

    @NotNull GuildItemList getTrophiesList();

    @NotNull GuildStoreItemList getStoreList();

    @NotNull GuildMenuSet getMenuSet();

    @NotNull GuildRankSet getRankSet();

    void broadcast(@NotNull final Message message);

}
