package fr.traqueur.endiapower.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IPower {

     String getName();

     ItemStack getIcon();

     int getId();

     int getMaxLevel();

     int getCountdown();

    void onUse(Player player);
}
