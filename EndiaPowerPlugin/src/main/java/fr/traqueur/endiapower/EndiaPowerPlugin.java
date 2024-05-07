package fr.traqueur.endiapower;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.commands.CommandManager;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.commands.EndiaPowerCommand;
import fr.traqueur.endiapower.commands.arguments.PowerArgument;
import fr.traqueur.endiapower.managers.PowerManager;
import fr.traqueur.endiapower.utils.CountdownUtils;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class EndiaPowerPlugin extends JavaPlugin {

    public static final String ADMIN_PERMISSION = "endiapower.admin";

    private IPowerManager powerManager;

    @Override
    public void onEnable() {
        CommandManager commandManager = new CommandManager(this);

        CountdownUtils.loadCountdownsFromFile(this);

        this.powerManager = new PowerManager();
        this.getServer().getServicesManager().register(IPowerManager.class, this.powerManager, this, ServicePriority.Normal);

        commandManager.registerConverter(IPower.class, "power", new PowerArgument(this.powerManager));
        commandManager.registerCommand(new EndiaPowerCommand(this));
    }

    @Override
    public void onDisable() {
        CountdownUtils.saveCountdownsToFile(this);
    }

    public IPowerManager getPowerManager() {
        return powerManager;
    }
}
