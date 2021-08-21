package com.luckyzz.guilds.language;

import com.akamecoder.api.Pathable;
import org.jetbrains.annotations.NotNull;

public enum Messages implements Pathable {

    MENU_PANE_CLICK("menu.paneClick"),
    ONLY_FOR_PLAYER("messages.onlyForPlayer"),
    NO_PERMISSION("messages.noPermission"),
    G_CREATE_USAGE("messages.gcreate.usage"),
    G_CREATE_SUCCESS_OWNER("messages.gcreate.created.owner"),
    G_CREATE_SUCCESS_ADMIN("messages.gcreate.created.admin"),
    G_CHAT_USAGE("messages.g.usage"),
    G_CHAT_NOT_GUILD("messages.g.notGuild"),
    G_CHAT_FORMAT("messages.g.format"),
    G_UPR_USAGE("messages.grank.usage"),
    G_UPR_NOT_GUILD("messages.grank.notGuild"),
    G_UPR_NOT_OWNER("messages.grank.notOwner"),
    G_UPR_NOT_YOURSELF("messages.grank.notYourself"),
    G_UPR_NOT_PLAYER("messages.grank.notPlayer"),
    G_UPR_ALREADY_UPR("messages.grank.already"),
    G_UPR_NOT_DIGIT("messages.grank.notDigit"),
    G_UPR_SUCCESS_SENDER("messages.grank.success.sender"),
    G_UPR_SUCCESS_GUILD("messages.grank.success.guild"),
    G_INVITE_REQUEST_NOT_ACCEPTED_REQUESTED("messages.ginvite.request.notAccepted.requested"),
    G_INVITE_REQUEST_NOT_ACCEPTED_REQUESTER("messages.ginvite.request.notAccepted.requester"),
    G_KICK_USAGE("messages.gkick.usage"),
    G_KICK_NOT_GUILD("messages.gkick.notGuild"),
    G_KICK_NOT_UPR("messages.gkick.notUpr"),
    G_KICK_NOT_YOURSELF("messages.gkick.notYourself"),
    G_KICK_NOT_PLAYER("messages.gkick.notPlayer"),
    G_KICK_PLAYER_UPR("messages.gkick.playerUpr"),
    G_KICK_SUCCESS_SENDER("messages.gkick.success.sender"),
    G_KICK_SUCCESS_GUILD("messages.gkick.success.guild"),
    G_INVITE_USAGE("messages.ginvite.usage"),
    G_INVITE_NOT_GUILD("messages.ginvite.notGuild"),
    G_INVITE_NOT_UPR("messages.ginvite.notUpr"),
    G_INVITE_NOT_YOURSELF("messages.ginvite.notYourself"),
    G_INVITE_ALREADY_IN_GUILD("messages.ginvite.alreadyInGuild"),
    G_INVITE_NOT_ONLINE("messages.ginvite.notOnline"),
    G_INVITE_SUCCESS_SENDER("messages.ginvite.success.sender"),
    G_INVITE_SUCCESS_SENT("messages.ginvite.success.sent"),
    G_ACCEPT_USAGE("messages.gaccept.usage"),
    G_ACCEPT_NOT_INVITES("messages.gaccept.notInvites"),
    G_ACCEPT_ALREADY_GUILD("messages.gaccept.alreadyGuild"),
    G_ACCEPT_SUCCESS_SENDER("messages.gaccept.success.sender"),
    G_ACCEPT_SUCCESS_GUILD("messages.gaccept.success.guild"),
    MENU_FOR_MEMBERS("menu.forMembers"),
    MENU_CLOSED("menu.closed"),
    MENU_UNAVAILABLE("menu.unavailable"),
    G_CREATE_NOT_ONLINE("messages.gcreate.notOnline"),
    GUILDS_LEVEL_UPDATE("messages.guild.levelUpdate"),
    MENU_PAGE_UNAVAILABLE("menu.pageUnavailable"),
    MENU_NOT_ENOUGH_MONEY("menu.notEnoughMoney"),
    NOT_POSSIBILITY("messages.notPossibility"),
    MENU_SUCCESS_BOUGHT("menu.successBought"),
    G_LEAVE_USAGE("messages.gleave.usage"),
    G_LEAVE_NOT_GUILD("messages.gleave.notGuild"),
    G_LEAVE_SUCCESS_SENDER("messages.gleave.success.sender"),
    G_LEAVE_SUCCESS_GUILD("messages.gleave.success.guild"),
    COOLDOWN_GOT("messages.cooldown.got"),
    COOLDOW_EXPIRED("messages.cooldown.expired"),
    COOLDOWN_CANNOT_ANOTHER("messages.cooldown.cannot.another"),
    COOLDOWN_CANNOT_WHO_GOT("messages.cooldown.cannot.whoGot"),
    G_LEAVE_CANNOT("messages.gleave.cannot"),
    G_TITLE_USAGE("messages.gtitle.usage"),
    G_TITLE_NOT_GUILD("messages.gtitle.notGuild"),
    G_TITLE_NOT_UPR("messages.gtitle.notUpr"),
    G_TITLE_ALREADY("messages.gtitle.already"),
    G_TITLE_NOT_DIGIT("messages.gtitle.notDigit"),
    G_TITLE_SUCCESS_SENDER("messages.gtitle.success.sender"),
    G_TITLE_SUCCESS_GUILD("messages.gtitle.success.guild"),
    G_LIST_USAGE("messages.glist.usage"),
    G_LIST_NOT_GUILD("messages.glist.notGuild"),
    G_LIST_NOT_UPR("messages.glist.notUpr"),
    G_LIST_FORMAT_TOP("messages.glist.format.top"),
    G_LIST_FORMAT_RANK("messages.glist.format.rank");

    private final String path;

    Messages(@NotNull final String path) {
        this.path = path;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }

    @Override
    public @NotNull String toString() {
        return "Messages{" +
                "path='" + path + '\'' +
                "} " + super.toString();
    }

}