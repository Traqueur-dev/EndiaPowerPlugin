package fr.traqueur.endiapower.commands.arguments;

import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IPowerManager;
import fr.traqueur.endiapower.api.commands.arguments.ArgumentConverter;
import fr.traqueur.endiapower.api.commands.arguments.TabConverter;
import fr.traqueur.endiapower.managers.PowerManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class PowerArgument implements ArgumentConverter<IPower>, TabConverter {

    private IPowerManager powerManager;

    public PowerArgument(IPowerManager powerManager) {
        this.powerManager = powerManager;
    }

    @Override
    public IPower apply(String input) {
        try {
            return this.powerManager.getPowerByName(input.replace("_", " "));
        } catch(NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public List<String> onCompletion() {
        return this.powerManager.getPowers().stream().map(p -> p.getName().replace(" ", "_")).collect(Collectors.toList());
    }
}
