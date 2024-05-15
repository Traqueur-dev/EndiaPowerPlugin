package fr.traqueur.endiapower;

import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.utils.CountdownUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class EndiaPlaceholder extends PlaceholderExpansion {

    private static final String ACCESS_TO = "access_to_";
    private static final String COOLDOWN = "cooldown_";
    private static final String LEVEL = "level_";

    private final IManager powerManager;

    private EndiaPlaceholder(EndiaPowerPlugin plugin) {
        this.powerManager = plugin.getPowerManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "endiapower";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Traqueur_";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1-DEV";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        LinkedList<String> paramsList = new LinkedList<>(List.of(params.split("_")));
        int powerId;
        IPower power;
        String returnValue;

        try{
            powerId = Integer.parseInt(paramsList.removeLast());
            power = powerManager.getPower(powerId);
        } catch (Exception e) {
            return "Error";
        }

        if (params.contains(ACCESS_TO)) {
            returnValue = powerManager.hasPower(player.getUniqueId(), powerId) ? "true" : "false";
        } else if (params.contains(COOLDOWN)) {
            String name = power.getName().replace(" ", "_");
            if (CountdownUtils.isOnCountdown(name, player.getUniqueId())) {
                returnValue = CountdownUtils.getCountdownRemaining(player.getUniqueId(), name);
            } else {
                returnValue = "X";
            }
        } else if (params.contains(LEVEL)) {
            returnValue = String.valueOf(powerManager.getPlayerPowers(player.getUniqueId()).getOrDefault(power, -1));
        } else {
            returnValue = "Error";
        }

        return returnValue;
    }

    public static void register(EndiaPowerPlugin plugin) {
        new EndiaPlaceholder(plugin).register();
    }
}
