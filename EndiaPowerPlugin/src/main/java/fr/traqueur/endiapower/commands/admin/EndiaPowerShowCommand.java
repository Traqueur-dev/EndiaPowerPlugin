package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

/**
 * Command to show a power
 */
public class EndiaPowerShowCommand extends EndiaCommand {

    public EndiaPowerShowCommand(EndiaPowerPlugin plugin) {
        super(plugin, "show");

        this.addAlias("view", "display", "see", "s");

        this.addArgs("power:power");
    }

    @Override
    public void execute(CommandSender sender, Arguments args) {
        IPower power = args.get("power");
        sender.sendMessage("§e§l" + power.getName() + " §7§l- §e§lMax: " + power.getMaxLevel());
    }
}
