package fr.traqueur.endiapower.api.events;

import fr.traqueur.endiapower.api.IPower;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The class EndiaPowerLaunchEvent.
 * <p> This class is used to represent the event when a player uses a power. </p>
 * */
public class EndiaPowerLaunchEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlersList() {
        return HANDLERS_LIST;
    }

    private final IPower power;
    private boolean cancel;

    /**
     * Constructor of the class EndiaPowerLaunchEvent.
     * @param who The player who uses the power.
     * @param power The power used.
     * */
    public EndiaPowerLaunchEvent(@NotNull Player who, IPower power) {
        super(who, false);
        this.power = power;
        this.cancel = false;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }

    public IPower getPower() {
        return power;
    }
}
