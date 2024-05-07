package fr.traqueur.endiapower.api.commands.exceptions;

/**
 * Cette exception est levée lorsqu'un argument n'existe pas.
 */
public class ArgumentNotExistException extends Exception {

    /**
     * Constructeur de la classe ArgumentNotExistException.
     */
    public ArgumentNotExistException() {
        super("Argument does not exist.");
    }
}
