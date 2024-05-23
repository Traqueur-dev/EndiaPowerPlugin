package fr.traqueur.endiapower.menus;

import fr.maxlego08.menu.ZInventory;
import fr.maxlego08.menu.api.button.Button;
import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.menus.buttons.PowerButton;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PowersInventory extends ZInventory {

	private final EndiaPowerPlugin plugin;

	public PowersInventory(Plugin plugin, String name, String fileName, int size, List<Button> buttons) {
		super(plugin, name, fileName, size, buttons);
		this.plugin = (EndiaPowerPlugin) plugin;
	}

	@Override
	public int getMaxPage(Player player, Object... objects) {
		int maxPage = super.getMaxPage(player, objects);
		int currentMaxPage = 1;

		Optional<PowerButton> optional = this.getButtons(PowerButton.class).stream().findFirst();
		if (optional.isPresent()) {
			PowerButton button = optional.get();

			HashMap<IPower, Integer> powers = this.plugin.getPowerManager().getAllPlayerPowers(player.getUniqueId());
			
			int elementSize = powers.size();
			if (elementSize >= 1) {
				int size = button.getSlots().size();
				return ((elementSize / (size)) + (elementSize == (size) ? 0 : 1));
			}
		}

		return Math.max(maxPage, currentMaxPage);
	}

}