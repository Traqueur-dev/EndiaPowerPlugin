package fr.traqueur.endiapower.api.commands.arguments;

import com.google.common.base.Function;

/**
 * Interface définissant un convertisseur d'argument.
 * @param <T> Le type de l'argument converti.
 */
public interface ArgumentConverter<T> extends Function<String, T> {
}
