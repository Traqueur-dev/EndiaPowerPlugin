package fr.traqueur.endiapower.commands;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

public class EndiaPowerCommand extends EndiaCommand {

    public EndiaPowerCommand(EndiaPowerPlugin plugin) {
        super(plugin, "endiapower");

        this.addAlias("power", "ep");
    }

    @Override
    public void execute(CommandSender sender, Arguments args) {

    }
}
