package fr.traqueur.endiapower.managers;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.models.Powers;
import fr.traqueur.endiapower.models.User;
import fr.traqueur.endiapower.utils.DiscUtils;

import java.io.File;
import java.util.*;

public class PowerManager implements IManager {

    private static final String PLAYERS_FILE = "players.json";
    private static final String POWERS_FILE = "powers.json";

    private final EndiaPowerPlugin plugin;
    private final Set<IPower> powers;
    private final HashMap<UUID, IUser> players;

    public PowerManager(EndiaPowerPlugin plugin) {
        this.plugin = plugin;
        this.powers = new HashSet<>();
        this.players = new HashMap<>();
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
        return this.powers.stream().filter(power -> power.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
    }

    public Set<IPower> getPowers() {
        return powers;
    }

    public HashMap<IPower, Integer> getPlayerPowers(UUID uuid) {
        return this.players.getOrDefault(uuid, new User(uuid)).getPowers();
    }

    public boolean hasPower(UUID uuid, int id) {
        return this.players.getOrDefault(uuid, new User(uuid)).getPowers().containsKey(this.getPower(id));
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

    public File getFile(String name) {
        return new File(this.plugin.getDataFolder(), name);
    }

    @Override
    public void loadData() {
        String content = DiscUtils.readCatch(this.getFile(POWERS_FILE));
        if (content != null) {
            TypeToken<Set<IPower>> type = new TypeToken<>() {
            };
            this.powers.addAll(plugin.getGson().fromJson(content, type.getType()));
        }
        //TODO:check why adapter doesn't work with this set
        if(this.powers.isEmpty()) {
            for (Powers value : Powers.values()) {
                this.createPower(value);
            }
        }

        String contentPlayers = DiscUtils.readCatch(this.getFile(PLAYERS_FILE));
        if (contentPlayers != null) {
            TypeToken<HashMap<UUID, IUser>> type = new TypeToken<>() {
            };
            this.players.putAll(plugin.getGson().fromJson(contentPlayers, type.getType()));
        }
    }

    @Override
    public void saveData() {
        DiscUtils.writeCatch(this.getFile(POWERS_FILE), plugin.getGson().toJson(this.powers));
        DiscUtils.writeCatch(this.getFile(PLAYERS_FILE), plugin.getGson().toJson(this.players));
    }
}
