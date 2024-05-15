package fr.traqueur.endiapower.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.utils.CountdownUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PowersProvider implements InventoryProvider {

    public static SmartInventory getInventory(EndiaPowerPlugin plugin) {
        return SmartInventory.builder()
                .provider(new PowersProvider(plugin))
                .manager(plugin.getInventoryManager())
                .size(6, 9)
                .title("Pouvoirs")
                .build();
    }

    private final EndiaPowerPlugin plugin;
    private final IManager powerManager;

    private PowersProvider(EndiaPowerPlugin plugin) {
        this.plugin = plugin;
        this.powerManager = plugin.getPowerManager();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        UUID uuid = player.getUniqueId();
        Pagination pagination = inventoryContents.pagination();
        List<ClickableItem> items = new ArrayList<>();

        this.powerManager.getPlayerPowers(uuid).forEach((power,level) -> {
            items.add(ClickableItem.of(power.getIcon(), e -> {
                player.closeInventory();
                String countdownName = power.getName().replace(" ", "_").toUpperCase();
                if (CountdownUtils.isOnCountdown(countdownName, uuid)) {
                    player.sendMessage(Component.text("Vous ne pouvez pas utiliser de pouvoir.", NamedTextColor.RED));
                    return;
                }
                power.onUse(player);
                CountdownUtils.addCountdown(countdownName, uuid, (int) TimeUnit.MINUTES.toSeconds(10));
            }));
        });

        pagination.setItems(items.toArray(ClickableItem[]::new));
        pagination.setItemsPerPage(45);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        inventoryContents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> getInventory(this.plugin).open(player, pagination.previous().getPage())));
        inventoryContents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> getInventory(this.plugin).open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {}
}
