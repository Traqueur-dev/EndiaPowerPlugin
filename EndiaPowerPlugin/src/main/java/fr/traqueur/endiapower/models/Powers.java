package fr.traqueur.endiapower.models;

import fr.traqueur.endiapower.api.IPower;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Powers implements IPower {

    ARRESTO_MEMENTOMUN(1,"Arresto Mementomun", 1, Material.STICK),

    ;

    private final int id;
    private final String name;
    private final int maxLevel;
    private final Material icon;

     Powers(int id, String name, int level, Material icon) {
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
    }

    public String getName() {
        return this.name;
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
}
