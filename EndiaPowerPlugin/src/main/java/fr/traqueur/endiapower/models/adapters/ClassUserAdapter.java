package fr.traqueur.endiapower.models.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.traqueur.endiapower.api.IManager;
import fr.traqueur.endiapower.api.IPower;
import fr.traqueur.endiapower.api.IUser;
import fr.traqueur.endiapower.models.PlayerUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClassUserAdapter extends TypeAdapter<Class<? extends IUser>> {

    private final IManager manager;

    public ClassUserAdapter(IManager manager) {
        this.manager = manager;
    }

    @Override
    public void write(JsonWriter jsonWriter, Class<? extends IUser> clazz) throws IOException {
        jsonWriter.value(clazz.getName());
    }

    @Override
    public Class<? extends IUser> read(JsonReader jsonReader) throws IOException {
        String clazzName = jsonReader.nextString();
        clazzName = clazzName.replace("class ", "");
        try {
            return Class.forName(clazzName).asSubclass(IUser.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
