package fr.traqueur.endiapower.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class CountdownUtils {
    private static final HashMap<String, HashMap<UUID, Long>> countdowns = new HashMap<>();

    private static final String FILE_NAME = "countdowns.yml";

    public static void saveCountdownsToFile(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), FILE_NAME);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : countdowns.keySet()) {
            HashMap<UUID, Long> innerMap = countdowns.get(key);
            for (UUID uuid : innerMap.keySet()) {
                String path = key + "." + uuid.toString();
                config.set(path, innerMap.get(uuid));
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "&cImpossible de sauvegarder les countdowns dans le fichier !");
        }
    }

    public static void loadCountdownsFromFile(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), FILE_NAME);
        if (!file.exists()) {
            plugin.getLogger().log(Level.WARNING,"&cLe fichier "+FILE_NAME+" n'existe pas !");
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            ConfigurationSection innerSection = config.getConfigurationSection(key);
            if (innerSection != null) {
                HashMap<UUID, Long> innerMap = new HashMap<>();
                for (String uuidStr : innerSection.getKeys(false)) {
                    UUID uuid = UUID.fromString(uuidStr);
                    long time = innerSection.getLong(uuidStr);
                    innerMap.put(uuid, time);
                }
                countdowns.put(key, innerMap);
            }
        }

        plugin.getLogger().info("&aLes countdowns ont été chargés depuis le fichier countdowns.yml !");
    }


    public static void createCountdown(String alias) {
        if (countdowns.containsKey(alias)) {
            return;
        }
        countdowns.put(alias, new HashMap<>());
    }

    public static void addCountdown(String alias, UUID player, int seconds) {
        if (!countdowns.containsKey(alias)) {
            CountdownUtils.createCountdown(alias);
        }
        long next = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        countdowns.get(alias).put(player, next);
    }

    public static String getCountdownRemaining(UUID player, String name) {
        return DurationFormatter.getRemaining(CountdownUtils.getCountdownForPlayerLong(name, player), true);
    }

    public static void removeCountdown(String alias, UUID player) {
        if (!countdowns.containsKey(alias)) {
            return;
        }
        countdowns.get(alias).remove(player);
    }

    public static HashMap<UUID, Long> getCountdownMap(String alias) {
        if (countdowns.containsKey(alias)) {
            return countdowns.get(alias);
        }
        return null;
    }

    public static boolean isOnCountdown(String alias, UUID player) {
        if (countdowns.containsKey(alias) && countdowns.get(alias).containsKey(player)
                && System.currentTimeMillis() <= countdowns.get(alias).get(player)) {
            return true;
        }
        return false;
    }

    public static int getCountdownForPlayerInt(String alias, UUID player) {
        return (int) CountdownUtils.getCountdownForPlayerLong(alias, player);
    }

    public static long getCountdownForPlayerLong(String alias, UUID player) {
        return countdowns.get(alias).get(player) - System.currentTimeMillis();
    }

    public static void clearCountdowns() {
        countdowns.clear();
    }
}