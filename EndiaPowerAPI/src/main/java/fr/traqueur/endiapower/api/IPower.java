package fr.traqueur.endiapower.api;

import org.bukkit.inventory.ItemStack;

public interface IPower {

     String getName();

     ItemStack getIcon();

     int getId();

     int getMaxLevel();
}
