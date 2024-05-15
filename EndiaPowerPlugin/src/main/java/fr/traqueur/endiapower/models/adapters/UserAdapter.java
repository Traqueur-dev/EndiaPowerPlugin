package fr.traqueur.endiapower.models.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.models.PlayerUser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserAdapter extends TypeAdapter<IUser> {

    private final IManager manager;

    public UserAdapter(IManager manager) {
        this.manager = manager;
    }

    @Override
    public void write(JsonWriter jsonWriter, IUser iUser) throws IOException {

        jsonWriter.beginObject();
        jsonWriter.name("class").value(iUser.getClass().getName());
        jsonWriter.name("uuid").value(iUser.getUUID().toString());
        jsonWriter.name("powers").beginObject();
        for (Map.Entry<IPower, Integer> entry : iUser.getPowers().entrySet()) {
            jsonWriter.name(entry.getKey().getName().replace(" ", "_")).value(entry.getValue());
        }
        jsonWriter.endObject();
        jsonWriter.endObject();
    }

    @Override
    public IUser read(JsonReader jsonReader) throws IOException {
        String className = null;
        UUID uuid = null;
        HashMap<IPower, Integer> powers = new HashMap<>();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "class" -> className = jsonReader.nextString();
                case "uuid" -> uuid = UUID.fromString(jsonReader.nextString());
                case "powers" -> {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        powers.put(manager.getPowerByName(jsonReader.nextName().replace("_", " ")), jsonReader.nextInt());
                    }
                    jsonReader.endObject();
                }
            }
        }
        jsonReader.endObject();

        try {
            return Class.forName(className).asSubclass(IUser.class).getConstructor(UUID.class, HashMap.class).newInstance(uuid, powers);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
