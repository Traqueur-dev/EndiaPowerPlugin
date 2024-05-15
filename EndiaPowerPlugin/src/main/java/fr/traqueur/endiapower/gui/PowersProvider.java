package fr.traqueur.endiapower.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.events.EndiaPowerLaunchEvent;
import fr.traqueur.endiapower.utils.CountdownUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public Pagination setupContent(Player player, InventoryContents inventoryContents) {
        UUID uuid = player.getUniqueId();
        Pagination pagination = inventoryContents.pagination();
        List<ClickableItem> items = new ArrayList<>();

        this.powerManager.getAllPlayerPowers(uuid).forEach((power, level) -> {
            ItemStack item = new ItemStack(power.getIcon());
            ItemMeta meta = item.getItemMeta();
            List<Component> lore = new ArrayList<>();

            meta.displayName(Component.text(power.getName(), NamedTextColor.YELLOW));
            lore.add(Component.text(PlaceholderAPI.setPlaceholders(player, "Niveau: %endiapower_level_"+power.getId() +"%"), NamedTextColor.GRAY));

            String countdown = PlaceholderAPI.setPlaceholders(player, "%endiapower_cooldown_" + power.getId()+"%");
            lore.add(Component.text("Countdown: " + (countdown.equals("X") ? "disponible" : countdown),countdown.equals("X") ? NamedTextColor.GREEN : NamedTextColor.RED));

            meta.lore(lore);
            item.setItemMeta(meta);

            items.add(ClickableItem.of(item, e -> {
                player.closeInventory();
                String countdownName = power.getName().replace(" ", "_").toUpperCase();
                if (CountdownUtils.isOnCountdown(countdownName, uuid)) {
                    player.sendMessage(Component.text("Vous ne pouvez pas utiliser de pouvoir.", NamedTextColor.RED));
                    return;
                }
                EndiaPowerLaunchEvent event = new EndiaPowerLaunchEvent(player, power);
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> Bukkit.getPluginManager().callEvent(event));
                if (event.isCancelled()) {
                    return;
                }
                power.onUse(player);
                CountdownUtils.addCountdown(countdownName, uuid, power.getCountdown());
            }));
        });

        pagination.setItems(items.toArray(ClickableItem[]::new));
        pagination.setItemsPerPage(45);
        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        return pagination;
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Pagination pagination = this.setupContent(player, inventoryContents);

        inventoryContents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> getInventory(this.plugin).open(player, pagination.previous().getPage())));
        inventoryContents.set(5, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> getInventory(this.plugin).open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {
        this.setupContent(player, inventoryContents);
    }
}
