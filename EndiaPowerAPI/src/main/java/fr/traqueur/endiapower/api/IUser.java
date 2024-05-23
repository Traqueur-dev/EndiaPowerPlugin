package fr.traqueur.endiapower.api;

import java.util.HashMap;
import java.util.UUID;

/**
 * The interface IUser.
 * <p> This interface is used to represent the users of the plugin. </p>
 */
public interface IUser {

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    UUID getUUID();

    /**
     * Gets powers.
     *
     * @return the powers with levels
     */
    HashMap<IPower, Integer> getPowers();

    /**
     * Grant power.
     * @param power
     * @param level
     */
    void grantPower(IPower power, int level);

    /**
     * Revoke power.
     * @param power
     */
    void revokePower(IPower power);

    /**
     * Check if player is bind with this user. Use this method for custom implementation.
     * @param player
     * @return true if player is bind with this user.
     */
    boolean hasPlayer(UUID player);
}
