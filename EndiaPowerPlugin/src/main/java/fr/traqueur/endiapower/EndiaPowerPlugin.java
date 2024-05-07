package fr.traqueur.endiapower;

import fr.traqueur.endiapower.managers.PowerManager;
import fr.traqueur.endiapower.utils.CountdownUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class EndiaPowerPlugin extends JavaPlugin {

    private PowerManager powerManager;

    @Override
    public void onEnable() {
        this.powerManager = new PowerManager();
        CountdownUtils.loadCountdownsFromFile(this);
    }

    @Override
    public void onDisable() {
        CountdownUtils.saveCountdownsToFile(this);
    }
}
