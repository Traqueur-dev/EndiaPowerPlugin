package fr.traqueur.endiapower.commands.arguments;

import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.commands.arguments.ArgumentConverter;
import fr.traqueur.endiapower.api.commands.arguments.TabConverter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Argument converter for the IPower interface
 */
public class PowerArgument implements ArgumentConverter<IPower>, TabConverter {

    private final IManager powerManager;

    public PowerArgument(IManager powerManager) {
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
