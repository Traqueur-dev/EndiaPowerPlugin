package fr.traqueur.endiapower.models;

import fr.traqueur.endiapower.api.IPower;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

/**
 * Enumération des pouvoirs
 */
public enum Powers implements IPower {

    ARRESTO_MEMENTOMUN(1,"Arresto Mementomun", 3, Material.STICK, (int) TimeUnit.MINUTES.toSeconds(10)) {
        @Override
        public void onUse(Player player) {}
    },

    ;

    private final int id;
    private final String name;
    private final int maxLevel;
    private final Material icon;
    private final int countdown;

     Powers(int id, String name, int level, Material icon, int countdown) {
        if(icon == Material.AIR) {
            throw new IllegalArgumentException("Icon must be not air");
        }
         if(level < 1) {
             throw new IllegalArgumentException("Level must be greater than 0");
         }
        this.id = id;
        this.name = name;
        this.maxLevel = level;
        this.icon = icon;
        this.countdown = countdown;
    }

    public String getName() {
        return this.name;
    }

    public String getCountdownName() {
        return this.name.replace(" ", "_").toUpperCase();
    }

    public ItemStack getIcon() {
        return new ItemStack(this.icon, 1);
    }

    public int getId() {
        return this.id;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getCountdown() {
         return countdown;
    }
}
