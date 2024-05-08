package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class EndiaPowerListPowersCommand extends EndiaCommand {

    private IManager powerManager;

    public EndiaPowerListPowersCommand(EndiaPowerPlugin plugin) {
        super(plugin, "listpowers");

        this.addAlias("list", "powers", "lp");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {
        Component message = Component.text("Liste des pouvoirs: \n", NamedTextColor.YELLOW);
        Set<IPower> powers = this.powerManager.getPowers();
        for (IPower power : powers) {
            message = message.append(Component.text("- ", NamedTextColor.GRAY)
                    .append(Component.text(power.getName(), NamedTextColor.YELLOW))
                    .append(Component.text(" (Max: " + power.getMaxLevel() + ")\n", NamedTextColor.GRAY)));
        }
        sender.sendMessage(message);
    }
}
