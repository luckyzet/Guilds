package com.luckyzz.guilds.guild;

import com.akamecoder.api.Identifiable;
import org.jetbrains.annotations.NotNull;

public interface GuildMember extends Identifiable<String> {

    @NotNull GuildRank getRank();

    void updateRank(@NotNull final GuildRank rank);

}
