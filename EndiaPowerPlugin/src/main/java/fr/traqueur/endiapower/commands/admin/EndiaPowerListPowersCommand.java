package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

public class EndiaPowerListPowersCommand extends EndiaCommand {

    private IPowerManager powerManager;

    public EndiaPowerListPowersCommand(EndiaPowerPlugin plugin) {
        super(plugin, "listpowers");

        this.addAlias("list", "powers", "lp");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {

    }
}
