package fr.traqueur.endiapower.commands;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import fr.traqueur.endiapower.commands.admin.*;
import org.bukkit.command.CommandSender;

public class EndiaPowerCommand extends EndiaCommand {

    private IPowerManager powerManager;

    public EndiaPowerCommand(EndiaPowerPlugin plugin) {
        super(plugin, "endiapower");

        this.addAlias("power", "ep");

        this.powerManager = plugin.getPowerManager();

        this.addSubCommand(new EndiaPowerGrantCommand(plugin), new EndiaPowerRevokeCommand(plugin),
                new EndiaPowerListPowersCommand(plugin), new EndiaPowerReloadCommand(plugin), new EndiaPowerShowCommand(plugin));

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender sender, Arguments args) {

    }
}
