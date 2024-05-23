package fr.traqueur.endiapower;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.button.loader.NoneLoader;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.api.commands.CommandManager;
import fr.traqueur.endiapower.commands.EndiaPowerCommand;
import fr.traqueur.endiapower.commands.arguments.PowerArgument;
import fr.traqueur.endiapower.hooks.FactionUser;
import fr.traqueur.endiapower.managers.PowerManager;
import fr.traqueur.endiapower.menus.PowersInventory;
import fr.traqueur.endiapower.menus.buttons.PowerButton;
import fr.traqueur.endiapower.models.adapters.ClassUserAdapter;
import fr.traqueur.endiapower.models.adapters.PowerAdapter;
import fr.traqueur.endiapower.models.adapters.UserAdapter;
import fr.traqueur.endiapower.utils.CountdownUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public final class EndiaPowerPlugin extends JavaPlugin {

    public static final String ADMIN_PERMISSION = "endiapower.admin";

    private Gson gson;
    private IManager powerManager;
    private InventoryManager inventoryManager;

    /**
     * Méthode appelée lorsque le plugin est chargé
     */
    @Override
    public void onLoad() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getLogger().severe("PlaceholderAPI not found...");
            this.getServer().shutdown();
        }
    }

    /**
     * Méthode appelée lorsque le plugin est activé
     */
    @Override
    public void onEnable() {
        CommandManager commandManager = new CommandManager(this);

        CountdownUtils.loadCountdownsFromFile(this);

        this.powerManager = new PowerManager(this);
        this.getServer().getServicesManager().register(IManager.class, this.powerManager, this, ServicePriority.Normal);

        this.inventoryManager = this.getProvider(InventoryManager.class);
        ButtonManager buttonManager = this.getProvider(ButtonManager.class);

        if(inventoryManager == null || buttonManager == null) {
            this.getLogger().severe("InventoryManager or ButtonManager not found...");
            this.getServer().shutdown();
            return;
        }

        buttonManager.unregisters(this);
        buttonManager.register(new NoneLoader(this, PowerButton.class, "endiapower_power"));

        inventoryManager.deleteInventories(this);
        try {
            this.inventoryManager.loadInventoryOrSaveResource(this,"powers_inventory.yml", PowersInventory.class);
        } catch (InventoryException e) {
            throw new RuntimeException(e);
        }

        this.powerManager.registerHook(FactionUser.class);

        this.gson = this.createGsonBuilder(this.powerManager).create();

        commandManager.registerConverter(IPower.class, "power", new PowerArgument(this.powerManager));
        commandManager.registerCommand(new EndiaPowerCommand(this));

        EndiaPlaceholder.register(this);

        //Permet de run la load à la toute fin du chargement de tout les plugins
        Bukkit.getScheduler().runTask(this, this.powerManager::loadData);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, powerManager::saveData, 0L, 20L * TimeUnit.HOURS.toSeconds(1));
    }

    /**
     * Méthode appelée lorsque le plugin est désactivé
     */
    @Override
    public void onDisable() {
        CountdownUtils.saveCountdownsToFile(this);
        this.powerManager.saveData();
    }

    /**
     * Crée un GsonBuilder avec les adapters nécessaires
     *
     * @param manager le manager
     * @return le GsonBuilder
     */
    private GsonBuilder createGsonBuilder(IManager manager) {
        GsonBuilder ret = new GsonBuilder();

        ret.setPrettyPrinting();
        ret.disableHtmlEscaping();

        ret.registerTypeHierarchyAdapter(IPower.class, new PowerAdapter());
        ret.registerTypeHierarchyAdapter(IUser.class, new UserAdapter(manager));
        ret.registerTypeHierarchyAdapter(Class.class, new ClassUserAdapter(manager));

        return ret;
    }

    /**
     * Récupère un provider
     *
     * @param classz la classe du provider
     * @param <T>    le type du provider
     * @return le provider
     */
    private <T> T getProvider(Class<T> classz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
        return provider == null ? null : provider.getProvider() != null ? (T) provider.getProvider() : null;
    }

    /**
     * Récupère le manager de power
     *
     * @return le manager de power
     */
    public IManager getPowerManager() {
        return this.powerManager;
    }

    /**
     * Récupère le manager d'inventaire
     *
     * @return le manager d'inventaire
     */
    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    /**
     * Récupère le Gson
     *
     * @return le Gson
     */
    public Gson getGson() {
        return this.gson;
    }
}
