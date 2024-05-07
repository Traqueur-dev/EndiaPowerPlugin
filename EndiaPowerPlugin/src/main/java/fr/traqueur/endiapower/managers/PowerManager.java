package fr.traqueur.endiapower.managers;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.models.Powers;

import java.util.*;

public class PowerManager {

    private final Set<IPower> powers;
    private final HashMap<UUID, Set<IPower>> playersPowers;

    public PowerManager() {
        this.powers = new HashSet<>();
        this.playersPowers = new HashMap<>();

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

    public Set<IPower> getPlayerPowers(UUID uuid) {
        return this.playersPowers.getOrDefault(uuid, new HashSet<>());
    }

    public void grantPower(UUID uuid, IPower power) {
        Set<IPower> powers = this.playersPowers.getOrDefault(uuid, new HashSet<>());
        powers.add(power);
        this.playersPowers.put(uuid, powers);
    }

    public void revokePower(UUID uuid, IPower power) {
        Set<IPower> powers = this.playersPowers.getOrDefault(uuid, new HashSet<>());
        powers.remove(power);
        this.playersPowers.put(uuid, powers);
    }

}
