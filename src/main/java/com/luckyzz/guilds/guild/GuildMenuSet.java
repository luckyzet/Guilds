package com.luckyzz.guilds.guild;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GuildMenuSet {

    private final List<GuildStoreMenu> storeMenus = new ArrayList<>();
    private final List<GuildBankMenu> bankMenus = new ArrayList<>();
    private final List<GuildTrophiesMenu> trophiesMenus = new ArrayList<>();
    private GuildUpgradeMenu upgradeMenu;

    GuildMenuSet() {
    }

    public @Nullable GuildUpgradeMenu getUpgradeMenu() {
        return upgradeMenu;
    }

    public void updateUpgradeMenu(@NotNull final GuildUpgradeMenu upgradeMenu) {
        this.upgradeMenu = upgradeMenu;
    }

    public @Nullable GuildStoreMenu getStoreMenu(final int page) {
        return storeMenus.stream().filter(menu -> menu.getPage() == page).findFirst().orElse(null);
    }

    public void registerStoreMenu(@NotNull final GuildStoreMenu storeMenu) {
        storeMenus.add(storeMenu);
    }

    public @NotNull List<GuildBankMenu> getBankMenuList() {
        return bankMenus;
    }

    public @Nullable GuildBankMenu getBankMenu(final int page) {
        return bankMenus.stream().filter(menu -> menu.getPage() == page).findFirst().orElse(null);
    }

    public void registerBankMenu(@NotNull final GuildBankMenu bankMenu) {
        bankMenus.add(bankMenu);
    }

    public @NotNull List<GuildTrophiesMenu> getTrophiesMenuList() {
        return trophiesMenus;
    }

    public @Nullable GuildTrophiesMenu getTrophiesMenu(final int page) {
        return trophiesMenus.stream().filter(menu -> menu.getPage() == page).findFirst().orElse(null);
    }

    public void registerTrophiesMenu(@NotNull final GuildTrophiesMenu trophiesMenu) {
        trophiesMenus.add(trophiesMenu);
    }

}
