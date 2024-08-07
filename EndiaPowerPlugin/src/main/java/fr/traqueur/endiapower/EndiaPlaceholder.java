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

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe.
     *
     * @param plugin Instance du plugin
     */
    private EndiaPlaceholder(EndiaPowerPlugin plugin) {
        this.powerManager = plugin.getPowerManager();
    }

    /**
     * Cette méthode doit retourner le nom du placeholder.
     */
    @Override
    public @NotNull String getIdentifier() {
        return "endiapower";
    }

    /**
     * Cette méthode doit retourner le nom de l'auteur du plugin.
     */
    @Override
    public @NotNull String getAuthor() {
        return "Traqueur_";
    }

    /**
     * Cette méthode doit retourner la version du plugin.
    */
    @Override
    public @NotNull String getVersion() {
        return "0.1-DEV";
    }

    /**
     * Cette méthode est appelée par PlaceholderAPI pour obtenir la valeur d'un placeholder.
     *
     * @param player Joueur qui demande le placeholder
     * @param params Paramètres du placeholder
     * @return La valeur du placeholder
     */
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
            if (CountdownUtils.isOnCountdown(power.getCountdownName(), player.getUniqueId())) {
                returnValue = CountdownUtils.getCountdownRemaining(player.getUniqueId(), power.getCountdownName());
            } else {
                returnValue = "X";
            }
        } else if (params.contains(LEVEL)) {
            returnValue = String.valueOf(powerManager.getAllPlayerPowers(player.getUniqueId()).getOrDefault(power, -1));
        } else {
            returnValue = "Error";
        }

        return returnValue;
    }

    /**
     * Enregistre le placeholder dans PlaceholderAPI.
     *
     * @param plugin Instance du plugin
     */
    public static void register(EndiaPowerPlugin plugin) {
        new EndiaPlaceholder(plugin).register();
    }
}
