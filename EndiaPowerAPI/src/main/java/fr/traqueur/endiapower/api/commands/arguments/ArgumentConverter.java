package fr.traqueur.endiapower.api.commands.arguments;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Interface définissant un convertisseur d'argument.
 * @param <T> Le type de l'argument converti.
 */
public interface ArgumentConverter<T> extends Function<String, T> {

    @Override
    T apply(String s);
}
