package fr.traqueur.endiapower.commands.admin;

import fr.traqueur.endiapower.EndiaPowerPlugin;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.commands.EndiaCommand;
import fr.traqueur.endiapower.api.commands.arguments.Arguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class EndiaPowerRevokeCommand extends EndiaCommand {

    private IManager powerManager;

    public EndiaPowerRevokeCommand(EndiaPowerPlugin plugin) {
        super(plugin, "revokepower");

        this.addAlias("powerrevoke", "revoke", "rp", "remove", "rm", "removepower", "powerremove");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.addArgs("player:player", "power:power");

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {
        Player player = args.get("player");
        UUID uuid = player.getUniqueId();
        IPower power = args.get("power");

        Set<IPower> powers = powerManager.getPlayerPowers(uuid).keySet();
        if(!powers.contains(power)) {
            sender.sendMessage(Component.text("Ce joueur n'a pas accès à ce pouvoir.", NamedTextColor.RED));
            return;
        }

        this.powerManager.revokePower(uuid, power);

        sender.sendMessage(Component.text("Vous avez enlevé le pouvoir " + power.getName() + " à " + player.getName() + ".", NamedTextColor.GREEN));
        player.sendMessage(Component.text("Vous avez perdu le pouvoir " + power.getName() + ".", NamedTextColor.GREEN));
    }
}
