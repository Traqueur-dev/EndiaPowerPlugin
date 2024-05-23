package fr.traqueur.endiapower.menus.buttons;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.events.EndiaPowerLaunchEvent;
import fr.traqueur.endiapower.utils.CountdownUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PowerButton extends ZButton implements PaginateButton {

    private final Plugin plugin;
    private final IManager powerManager;

    public PowerButton(Plugin plugin) {
        this.plugin = plugin;
        this.powerManager = ((EndiaPowerPlugin) plugin).getPowerManager();
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {
        Pagination<IPower> pagination = new Pagination<>();
        HashMap<IPower, Integer> powers = this.powerManager.getAllPlayerPowers(player.getUniqueId());
        List<IPower> buttons = pagination.paginate(List.copyOf(powers.keySet()), this.slots.size(), inventory.getPage());

        for (int i = 0; i != Math.min(buttons.size(), this.slots.size()); i++) {
            int slot = slots.get(i);
            IPower power = buttons.get(i);
            UUID uuid = player.getUniqueId();

            inventory.addItem(slot, this.powerManager.getFormattedIcon(power, player)).setClick(event -> {
                if (CountdownUtils.isOnCountdown(power.getCountdownName(), uuid)) {
                    player.sendMessage(Component.text("Vous ne pouvez pas utiliser de pouvoir.", NamedTextColor.RED));
                    return;
                }
                EndiaPowerLaunchEvent eventPower = new EndiaPowerLaunchEvent(player, power);
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> Bukkit.getPluginManager().callEvent(eventPower));
                if (event.isCancelled()) {
                    return;
                }
                power.onUse(player);
                player.sendMessage(Component.text("Vous venez d'utiliser un pouvoir.", NamedTextColor.GREEN));
                CountdownUtils.addCountdown(power.getCountdownName(), uuid, power.getCountdown());
                player.closeInventory();
            });
        }
    }

    @Override
    public int getPaginationSize(Player player) {
        return this.powerManager.getAllPlayerPowers(player.getUniqueId()).size();
    }
}
