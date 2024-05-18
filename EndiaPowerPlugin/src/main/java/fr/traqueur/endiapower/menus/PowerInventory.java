package fr.traqueur.endiapower.menus;

import fr.maxlego08.menu.ZInventory;
import fr.maxlego08.menu.api.button.Button;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PowerInventory extends ZInventory {

    public PowerInventory(Plugin plugin, String name, String fileName, int size, List<Button> buttons) {
        super(plugin, name, fileName, size, buttons);
    }
}
