package fr.traqueur.endiapower.models.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.traqueur.endiapower.api.IPower;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class PowerAdapter extends TypeAdapter<IPower> {

    @Override
    public void write(JsonWriter jsonWriter, IPower power) throws IOException {

        jsonWriter.beginObject();
        jsonWriter.name("id").value(power.getId());
        jsonWriter.name("name").value(power.getName());
        jsonWriter.name("maxLevel").value(power.getMaxLevel());
        jsonWriter.name("countdown").value(power.getCountdown());
        jsonWriter.name("countdownName").value(power.getCountdownName());
        jsonWriter.name("icon").value(power.getIcon().getType().name());
        jsonWriter.endObject();

    }

    @Override
    public IPower read(JsonReader jsonReader) throws IOException {
        int id = 0;
        String name = null;
        String countdownName = null;
        int maxLevel = 0;
        Material icon = null;
        int countdown = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "id" -> id = jsonReader.nextInt();
                case "name" -> name = jsonReader.nextString();
                case "maxLevel" -> maxLevel = jsonReader.nextInt();
                case "icon" -> icon = Material.getMaterial(jsonReader.nextString());
                case "countdown" -> countdown = jsonReader.nextInt();
                case "countdownName" -> countdownName = jsonReader.nextString();
            }
        }
        jsonReader.endObject();

        return new Power(id, name, maxLevel, icon, countdown, countdownName);
    }

    private record Power(int id, String name, int maxLevel, Material icon, int countdown, String countdownName) implements IPower {

        private Power {
            if(icon == Material.AIR) {
                throw new IllegalArgumentException("Icon must be not air");
            }
            if(maxLevel < 1) {
                throw new IllegalArgumentException("Level must be greater than 0");
            }
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public ItemStack getIcon() {
            return new ItemStack(this.icon, 1);
        }

        @Override
        public int getId() {
            return this.id;
        }

        @Override
        public int getMaxLevel() {
            return this.maxLevel;
        }

        @Override
        public int getCountdown() {
            return this.countdown;
        }

        @Override
        public String getCountdownName() {
            return this.countdownName;
        }

        @Override
        public void onUse(Player player) {}
    }
}
