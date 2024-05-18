package fr.traqueur.endiapower.hooks;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;

import java.util.HashMap;
import java.util.UUID;

/**
 * Classe représentant un utilisateur de faction
 */
public record FactionUser(UUID uuid, HashMap<IPower, Integer> powers) implements IUser {

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public HashMap<IPower, Integer> getPowers() {
        return this.powers;
    }

    @Override
    public void grantPower(IPower power, int level) {
        this.powers.put(power, level);
    }

    @Override
    public void revokePower(IPower power) {
        this.powers.remove(power);
    }

    @Override
    public boolean hasPlayer(UUID player) {
        //get faction, check if player is in faction
        //but we don't have the faction plugin
        return true;
    }
}
