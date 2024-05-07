package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

public class EndiaPowerGrantCommand extends EndiaCommand {

    private IPowerManager powerManager;

    public EndiaPowerGrantCommand(EndiaPowerPlugin plugin) {
        super(plugin, "grantpower");

        this.addAlias("powergrant", "grantpower", "gp", "addpower", "poweradd", "addpower", "poweradd");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.addArgs("player:player", "power:power");
        this.addOptinalArgs("int:int");

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {

    }
}
