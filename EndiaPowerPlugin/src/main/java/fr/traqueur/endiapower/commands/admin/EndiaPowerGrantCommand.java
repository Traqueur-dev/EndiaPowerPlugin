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

import java.util.*;

public class EndiaPowerGrantCommand extends EndiaCommand {

    private final IManager powerManager;

    public EndiaPowerGrantCommand(EndiaPowerPlugin plugin) {
        super(plugin, "grantpower");

        this.addAlias("powergrant", "grantpower", "gp", "addpower", "poweradd", "addpower", "poweradd");
        this.setPermission(EndiaPowerPlugin.ADMIN_PERMISSION);

        this.addArgs("player:player", "power:power");
        this.addOptinalArgs("int:int", () -> List.of("1","2","3","4","5","6","7","8","9","10"));

        this.powerManager = plugin.getPowerManager();

    }

    @Override
    public void execute(CommandSender sender, Arguments args) {
        Player player = args.get("player");
        UUID uuid = player.getUniqueId();
        IPower power = args.get("power");
        Optional<Integer> optLevel = args.getOptional("int");
        int level = optLevel.orElse(1);

        HashMap<IPower, Integer> powers = powerManager.getPlayerPowers(uuid);
        if(this.powerManager.hasPower(uuid,power.getId()) && powers.get(power) == level) {
            sender.sendMessage(Component.text("Ce joueur a déjà ce pouvoir.", NamedTextColor.RED));
            return;
        }

        try {
            this.powerManager.grantPower(uuid, power, level);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
            return;
        }
        sender.sendMessage(Component.text("Vous avez donné le pouvoir " + power.getName() + " de niveau " + level + " à " + player.getName() + ".", NamedTextColor.GREEN));
        player.sendMessage(Component.text("Vous avez reçu le pouvoir " + power.getName() + " de niveau " + level + ".", NamedTextColor.GREEN));
    }
}
