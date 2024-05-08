package fr.traqueur.endiapower.managers;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.models.Powers;
import fr.traqueur.endiapower.models.User;

import java.util.*;

public class PowerManager implements IManager {

    private final Set<IPower> powers;
    private final HashMap<UUID, IUser> players;

    public PowerManager() {
        this.powers = new HashSet<>();
        this.players = new HashMap<>();

        for (Powers value : Powers.values()) {
            this.createPower(value);
        }
    }

    public void createPower(IPower power) {
        this.powers.add(power);
    }

    public void removePower(int id) throws NoSuchElementException {
        IPower power = this.getPower(id);
        this.powers.remove(power);
    }

    public IPower getPower(int id) throws NoSuchElementException {
        return this.powers.stream().filter(power -> power.getId() == id).findFirst().orElseThrow();
    }

    public IPower getPowerByName(String name) throws NoSuchElementException {
        return this.powers.stream().filter(power -> power.getName().equals(name)).findFirst().orElseThrow();
    }

    public Set<IPower> getPowers() {
        return powers;
    }

    public HashMap<IPower, Integer> getPlayerPowers(UUID uuid) {
        return this.players.getOrDefault(uuid, new User(uuid)).getPowers();
    }

    public void grantPower(UUID uuid, IPower power, int level) throws IllegalArgumentException {
        IUser user = this.players.getOrDefault(uuid, new User(uuid));
        if (power.getMaxLevel() < level) {
            throw new IllegalArgumentException("The level of the power is too high. (Max: " + power.getMaxLevel() + ")");
        }
        user.grantPower(power, level);
        this.players.put(uuid, user);
    }

    public void revokePower(UUID uuid, IPower power) {
        IUser user = this.players.getOrDefault(uuid, new User(uuid));
        user.revokePower(power);
        this.players.put(uuid, user);
    }

}
