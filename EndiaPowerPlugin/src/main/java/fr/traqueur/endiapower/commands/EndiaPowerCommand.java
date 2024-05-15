package fr.traqueur.endiapower.commands;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import fr.traqueur.endiapower.commands.admin.*;
import fr.traqueur.endiapower.gui.PowersProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndiaPowerCommand extends EndiaCommand {

    private final EndiaPowerPlugin plugin;

    public EndiaPowerCommand(EndiaPowerPlugin plugin) {
        super(plugin, "endiapower");

        this.addAlias("power", "ep");

        this.plugin = plugin;

        this.addSubCommand(new EndiaPowerGrantCommand(plugin), new EndiaPowerRevokeCommand(plugin),
                new EndiaPowerListPowersCommand(plugin), new EndiaPowerReloadCommand(plugin), new EndiaPowerShowCommand(plugin));

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender sender, Arguments args) {
        Player player = (Player) sender;
        PowersProvider.getInventory(this.plugin).open(player);
    }
}
