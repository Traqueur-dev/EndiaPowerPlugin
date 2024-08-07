package fr.traqueur.endiapower.managers;

import com.google.gson.reflect.TypeToken;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.hooks.FactionUser;
import fr.traqueur.endiapower.models.Powers;
import fr.traqueur.endiapower.models.PlayerUser;
import fr.traqueur.endiapower.utils.CountdownUtils;
import fr.traqueur.endiapower.utils.DiscUtils;
import it.unimi.dsi.fastutil.Hash;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PowerManager implements IManager {

    private static final String USERS_FILE = "users.json";
    private static final String POWERS_FILE = "powers.json";
    private static final TypeToken<Set<IPower>> TYPE_POWERS = new TypeToken<>() {};
    private static final TypeToken<Map<Class<? extends IUser>, HashMap<UUID, IUser>>> TYPE_USERS = new TypeToken<>() {};

    private final EndiaPowerPlugin plugin;
    private final Set<IPower> powers;
    private final Map<Class<? extends IUser>, HashMap<UUID, IUser>> users;

    public PowerManager(EndiaPowerPlugin plugin) {
        this.plugin = plugin;
        this.powers = new HashSet<>();
        this.users = new ConcurrentHashMap<>();
    }

    public void registerHook(Class<? extends IUser> clazz) {
        this.users.put(clazz, new HashMap<>());
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

    public HashMap<IPower, Integer> getOnlyPlayerPowers(UUID uuid) {
        HashMap<IPower, Integer> playersPowers = this.users.getOrDefault(PlayerUser.class, new HashMap<>()).getOrDefault(uuid, new PlayerUser(uuid)).getPowers();
        return playersPowers;
    }

    public HashMap<IPower, Integer> getAllPlayerPowers(UUID uuid) {
        HashMap<IPower, Integer> playersPowers = this.users.getOrDefault(PlayerUser.class, new HashMap<>()).getOrDefault(uuid, new PlayerUser(uuid)).getPowers();

        this.users.forEach((clazz, map) -> {
            if (!clazz.equals(PlayerUser.class)) {

                map.forEach((uuidUser, user) -> {
                    if (user.hasPlayer(uuid)) {
                        user.getPowers().forEach((power, level) -> {
                            if (playersPowers.containsKey(power)) {
                                playersPowers.put(power, Math.max(playersPowers.get(power), level));
                            } else {
                                playersPowers.put(power, level);
                            }
                        });
                    }
                });

            }
        });
        return playersPowers;
    }
    
    public IUser getUser(UUID uuid, Class<? extends IUser> clazz) {
        AtomicReference<IUser> user = new AtomicReference<>();
        this.users.get(clazz).forEach((uuidUser, userInner) -> {
            if (uuidUser.equals(uuid)) {
                user.set(userInner);
            }
        });
        return user.get();
    }

    public void addUser(UUID uuid, Class<? extends IUser> clazz) {
        try {
            IUser user = clazz.getDeclaredConstructor(UUID.class, HashMap.class).newInstance(uuid, new HashMap<>());
            HashMap<UUID, IUser> users = this.users.getOrDefault(clazz, new HashMap<>());
            users.put(uuid, user);
            this.users.put(clazz, users);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("The user class must have a constructor with UUID and HashMap<IPower, Integer> parameters.", e);
        }
    }

    public boolean hasPower(UUID uuid, int id) {
        IUser user = null;
        for (Class<? extends IUser> clzz : this.users.keySet()) {
            user = this.getUser(uuid, clzz);
            if(user != null) {
                break;
            }
        }
        return user != null && user.getPowers().containsKey(this.getPower(id));
    }

    public void grantPower(UUID uuid, IPower power, int level) throws IllegalArgumentException, IndexOutOfBoundsException {
        IUser user = null;
        for (Class<? extends IUser> clzz : this.users.keySet()) {
            user = this.getUser(uuid, clzz);
            if(user != null) {
                break;
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("The user is not found.");
        }

        if (power.getMaxLevel() < level) {
            throw new IndexOutOfBoundsException("The level of the power is too high. (Max: " + power.getMaxLevel() + ")");
        }
        user.grantPower(power, level);
        HashMap<UUID, IUser> users = this.users.get(user.getClass());
        users.put(uuid, user);
        this.users.put(user.getClass(), users);
    }

    public void revokePower(UUID uuid, IPower power) {
        IUser user = null;
        for (Class<? extends IUser> clzz : this.users.keySet()) {
            user = this.getUser(uuid, clzz);
            if(user != null) {
                break;
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("The user is not found.");
        }
        user.revokePower(power);
        HashMap<UUID, IUser> users = this.users.get(user.getClass());
        users.put(uuid, user);
        this.users.put(user.getClass(), users);
    }

    public ItemStack getFormattedIcon(IPower power, Player player) {
        ItemStack item = new ItemStack(power.getIcon());
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();

        meta.displayName(Component.text(power.getName(), NamedTextColor.YELLOW));
        lore.add(Component.text(PlaceholderAPI.setPlaceholders(player, "Niveau: %endiapower_level_"+power.getId() +"%"), NamedTextColor.GRAY));

        String countdown = PlaceholderAPI.setPlaceholders(player, "%endiapower_cooldown_" + power.getId()+"%");
        lore.add(Component.text("Countdown: " + (countdown.equals("X") ? "disponible" : countdown),countdown.equals("X") ? NamedTextColor.GREEN : NamedTextColor.RED));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public File getFile(String name) {
        return new File(this.plugin.getDataFolder(), name);
    }

    @Override
    public void loadData() {
        String content = DiscUtils.readCatch(this.getFile(POWERS_FILE));
        if (content != null) {
            this.powers.addAll(plugin.getGson().fromJson(content, TYPE_POWERS.getType()));
        }

        if(this.powers.isEmpty()) {
            for (Powers value : Powers.values()) {
                this.createPower(value);
            }
        }

        String contentUsers = DiscUtils.readCatch(this.getFile(USERS_FILE));
        if (contentUsers != null) {
            this.users.putAll(plugin.getGson().fromJson(contentUsers, TYPE_USERS.getType()));
        }
    }

    @Override
    public void saveData() {
        DiscUtils.writeCatch(this.getFile(POWERS_FILE), plugin.getGson().toJson(this.powers));
        DiscUtils.writeCatch(this.getFile(USERS_FILE), plugin.getGson().toJson(this.users));
    }

    @Override
    public void reload() {
        Set<IPower> oldPowers = new HashSet<>(this.powers);
        Map<Class<? extends IUser>, HashMap<UUID, IUser>> oldUsers = new ConcurrentHashMap<>(this.users);

        try {
            CountdownUtils.loadCountdownsFromFile(plugin);
        } catch (Exception e) {
            plugin.getLogger().severe("An error occurred while loading countdowns on reload.");
            return;
        }

        String content = DiscUtils.readCatch(this.getFile(POWERS_FILE));
        if (content != null) {
            this.powers.clear();
            try {
                this.powers.addAll(plugin.getGson().fromJson(content, TYPE_POWERS.getType()));
            } catch (Exception e) {
                this.powers.addAll(oldPowers);
                plugin.getLogger().severe("An error occurred while loading powers. The old powers have been restored.");
            }
        }

        String contentUsers = DiscUtils.readCatch(this.getFile(USERS_FILE));
        if (contentUsers != null) {
            this.users.clear();
            try {
                this.users.putAll(plugin.getGson().fromJson(contentUsers, TYPE_USERS.getType()));
            } catch (Exception e) {
                this.users.putAll(oldUsers);

                this.users.forEach((clazz, map)
                        -> map.forEach((uuid, user)
                        -> user.getPowers().entrySet().iterator().forEachRemaining(entry -> {
                    IPower power = entry.getKey();
                    int level = entry.getValue();
                    if (this.powers.stream().anyMatch(power1 -> power1.getName().equalsIgnoreCase(power.getName()))) {
                        user.revokePower(power);
                        user.grantPower(this.powers.stream().filter(power1 -> power1.getName().equalsIgnoreCase(power.getName())).findFirst().orElse(null), level);
                    }

                    if (this.powers.stream().anyMatch(power1 -> power1.getId() == power.getId())) {
                        user.revokePower(power);
                        user.grantPower(this.powers.stream().filter(power1 -> power1.getId() == power.getId()).findFirst().orElse(null), level);
                    }

                    if (this.powers.contains(power) && this.getPower(power.getId()).getMaxLevel() < level) {
                        user.revokePower(power);
                        user.grantPower(power, power.getMaxLevel());
                    }

                    if (!this.powers.contains(power)) {
                        user.revokePower(power);
                    }

                })));
            }
        }

        this.saveData();
        CountdownUtils.saveCountdownsToFile(plugin);
    }
}
