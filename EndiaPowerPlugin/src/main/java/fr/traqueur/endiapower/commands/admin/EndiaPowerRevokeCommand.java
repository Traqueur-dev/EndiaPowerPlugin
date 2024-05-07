package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

public class EndiaPowerRevokeCommand extends EndiaCommand {

    private IPowerManager powerManager;

    public EndiaPowerRevokeCommand(EndiaPowerPlugin plugin) {
        super(plugin, "revokepower");

        this.addAlias("powerrevoke", "revoke", "rp", "remove", "rm", "removepower", "powerremove");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.addArgs("player:player", "power:power");

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {

    }
}
