package fr.traqueur.endiapower.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

/**
 * The interface IManager.
 * <p> This interface is used to manage the powers and the users of the plugin. </p>
 */
public interface IManager {

      /**
       * Register hook.
       *
       * @param clazz the user class implementing IUser interface to register
       */
      void registerHook(Class<? extends IUser> clazz);

      /**
       * Register power.
       *
       * @param power the power to register
       */
      void createPower(IPower power);

        /**
         * Remove power.
         *
         * @param id the id of the power to remove
         * @throws NoSuchElementException if the power is not found
         */
      void removePower(int id) throws NoSuchElementException;

        /**
         * Get power.
         *
         * @param id the power to get
         * @return the power
         * @throws NoSuchElementException if the power is not found
         */
      IPower getPower(int id) throws NoSuchElementException;

        /**
         * Get power by name.
         *
         * @param name the name of the power to get
         * @return the power
         * @throws NoSuchElementException if the power is not found
         */
      IPower getPowerByName(String name) throws NoSuchElementException;

        /**
         * Get all powers.
         *
         * @return the set of all powers
         */
      Set<IPower> getPowers();

        /**
         * Get all powers from all implementations of users
         *
         * @param uuid the uuid of the player
         * @return the map of all powers of the specified player
         */
      HashMap<IPower, Integer> getAllPlayerPowers(UUID uuid);

      /**
       * Get all powers from PlayerUser implementation
       *
       * @param uuid the uuid of the player
       * @return the map of all powers of the specified player
       */
      HashMap<IPower, Integer> getOnlyPlayerPowers(UUID uuid);

      /**
       * Get user from uuid and specific IUser implementation
       * @param uuid
       * @param clazz
       * @return IUser
       */
      IUser getUser(UUID uuid, Class<? extends IUser> clazz);

      /**
       * add user with uuid player and specific IUser implementation
       * @param uuid
       * @param clazz
       * @return
       */
      void addUser(UUID uuid, Class<? extends IUser> user);

      /**
       * check if the user has the power with specific id
       * @param uuid
       * @param id
       * @return boolean
       */
      boolean hasPower(UUID uuid, int id);

      /**
       * Grant power to the user
       * @param uuid
       * @param power
       * @param level
       */
      void grantPower(UUID uuid, IPower power, int level);

      /**
       * Grant power to the user
       * @param uuid
       * @param power
       */
      void revokePower(UUID uuid, IPower power);

      ItemStack getFormattedIcon(IPower power, Player player);

      /**
       * Load data from the plugin
       */
      void loadData();

      /**
       * Save data from the plugin
       */
      void saveData();

      /**
       * Reload data from the plugin
       */
      void reload();
}
