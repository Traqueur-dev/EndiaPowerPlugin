package fr.traqueur.endiapower.menus;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.traqueur.endiapower.api.IManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PowerButton extends ZButton {

    private final IManager powerManager;

    public PowerButton(IManager powerManager) {
        super();
        this.powerManager = powerManager;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {

    }
}
