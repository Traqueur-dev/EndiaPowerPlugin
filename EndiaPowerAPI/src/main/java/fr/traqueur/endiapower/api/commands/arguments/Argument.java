package fr.traqueur.endiapower.api.commands.arguments;

import java.util.List;

/**
 * The class Argument.
 * <p> This class is used to represent an argument of a command. </p>
 */
public record Argument(String arg, List<String> completion) {}
