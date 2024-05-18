package fr.traqueur.endiapower.menus;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.button.ZButton;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PowerLoader implements ButtonLoader {

    private final Plugin plugin;
    private final IManager manager;
    private final Class<? extends ZButton> classz;
    private final String name;

    public PowerLoader(EndiaPowerPlugin plugin, Class<? extends ZButton> classz, String name) {
        this.plugin = plugin;
        this.manager = plugin.getPowerManager();
        this.classz = classz;
        this.name = name;
    }

    public Class<? extends Button> getButton() {
        return this.classz;
    }

    public String getName() {
        return this.name;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        try {
            Constructor<? extends ZButton> constructor = this.classz.getConstructor(IManager.class);
            return constructor.newInstance(this.manager);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException |
                 InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
