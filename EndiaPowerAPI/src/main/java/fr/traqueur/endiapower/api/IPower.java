package fr.traqueur.endiapower.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IPower {

     String getName();

     String getCountdownName();

     ItemStack getIcon();

     int getId();

     int getMaxLevel();

     int getCountdown();

    void onUse(Player player);
}
