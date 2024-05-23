package fr.traqueur.endiapower.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The interface IPower.
 * <p> This interface is used to represent the powers of the plugin. </p>
 */
public interface IPower {

     /**
      * Gets the name of the power.
      *
      * @return the name
      */
     String getName();

     String getCountdownName();

     /**
      * Gets the icon of the power.
      *
      * @return the icon
      */
     ItemStack getIcon();

        /**
        * Gets the id of the power.
        *
        * @return the id
        */
     int getId();

        /**
        * Gets the max level of the power.
        *
        * @return the max level
        */
     int getMaxLevel();

        /**
        * Gets the countdown for using power.
        *
        * @return the countdown
        */
     int getCountdown();

     /**
      * Call when the player use the power.
      * @param player
      */
     void onUse(Player player);
}
