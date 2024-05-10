package fr.traqueur.endiapower;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.api.commands.CommandManager;
import fr.traqueur.endiapower.commands.EndiaPowerCommand;
import fr.traqueur.endiapower.commands.arguments.PowerArgument;
import fr.traqueur.endiapower.managers.PowerManager;
import fr.traqueur.endiapower.models.adapters.PowerAdapter;
import fr.traqueur.endiapower.models.adapters.UserAdapter;
import fr.traqueur.endiapower.utils.CountdownUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public final class EndiaPowerPlugin extends JavaPlugin {

    public static final String ADMIN_PERMISSION = "endiapower.admin";

    private Gson gson;
    private IManager powerManager;

    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getLogger().severe("PlaceholderAPI not found...");
            this.getServer().shutdown();
        }

    }

    @Override
    public void onEnable() {
        CommandManager commandManager = new CommandManager(this);

        CountdownUtils.loadCountdownsFromFile(this);

        this.powerManager = new PowerManager(this);
        this.getServer().getServicesManager().register(IManager.class, this.powerManager, this, ServicePriority.Normal);


        this.gson = this.createGsonBuilder(this.powerManager).create();

        this.powerManager.loadData();

        commandManager.registerConverter(IPower.class, "power", new PowerArgument(this.powerManager));
        commandManager.registerCommand(new EndiaPowerCommand(this));

        EndiaPlaceholder.register(this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, powerManager::saveData, 0L, 20L * TimeUnit.HOURS.toSeconds(1));
    }

    @Override
    public void onDisable() {
        CountdownUtils.saveCountdownsToFile(this);
        this.powerManager.saveData();
    }

    private GsonBuilder createGsonBuilder(IManager manager) {
        GsonBuilder ret = new GsonBuilder();

        ret.setPrettyPrinting();
        ret.disableHtmlEscaping();

        ret.registerTypeHierarchyAdapter(IPower.class, new PowerAdapter());
        ret.registerTypeHierarchyAdapter(IUser.class, new UserAdapter(manager));

        return ret;
    }

    public IManager getPowerManager() {
        return this.powerManager;
    }

    public Gson getGson() {
        return this.gson;
    }
}
