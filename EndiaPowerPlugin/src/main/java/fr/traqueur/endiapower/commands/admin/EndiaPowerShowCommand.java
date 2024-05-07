package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

public class EndiaPowerShowCommand extends EndiaCommand {

    private IPowerManager powerManager;

    public EndiaPowerShowCommand(EndiaPowerPlugin plugin) {
        super(plugin, "show");

        this.addAlias("view", "display", "see", "s");

        this.addArgs("power:power");

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {

    }
}
