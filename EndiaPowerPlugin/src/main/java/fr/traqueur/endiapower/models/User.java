package fr.traqueur.endiapower.models;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;

import java.util.HashMap;
import java.util.UUID;

public record User(UUID uuid, HashMap<IPower, Integer> powers) implements IUser {

    public User(UUID uuid) {
        this(uuid, new HashMap<>());
    }

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


}
