package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import org.bukkit.command.CommandSender;

/**
 * Command to reload the plugin
 */
public class EndiaPowerReloadCommand extends EndiaCommand {

    private final IManager powerManager;

    public EndiaPowerReloadCommand(EndiaPowerPlugin plugin) {
        super(plugin, "reload");

        this.addAlias("rl");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {
        this.powerManager.reload();
        sender.sendMessage("§aLe plugin a été rechargé.");
    }
}
