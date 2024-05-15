package fr.traqueur.endiapower.models;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public record PlayerUser(UUID uuid, HashMap<IPower, Integer> powers) implements IUser {

    public PlayerUser(UUID uuid) {
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

    @Override
    public boolean hasPlayer(UUID player) {
        return this.uuid.equals(player);
    }


}
