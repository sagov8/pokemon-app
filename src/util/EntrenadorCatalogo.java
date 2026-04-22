package util;

import model.pokemon.Entrenador;

import java.util.List;

public class EntrenadorCatalogo {
    private EntrenadorCatalogo() {}

    public static List<Entrenador> getRivalesPredefinidos() {
        return List.of(crearGary(), crearMisty(), crearBrock());
    }

    /**
     * Rival escalado al nivel promedio del jugador.
     * Elige pokemon del catálogo cuyo tipo sea variado.
     */
    public static Entrenador crearRivalEscalado(int nivelPromedio) {
        Entrenador rival;

        if (nivelPromedio <= 7) {
            rival = crearGary();
        } else if (nivelPromedio <= 12) {
            rival = crearMisty();
        } else {
            rival = crearBrock();
        }

        return rival;
    }

    // Rivales predefinidos

    private static Entrenador crearGary() {
        Entrenador gary = new Entrenador(10, "Gary", 500);
        gary.agregarPokemonEquipo(PokemonCatalogo.crearSquirtle());
        gary.agregarPokemonEquipo(PokemonCatalogo.crearPidgey());
        return gary;
    }

    private static Entrenador crearMisty() {
        Entrenador misty = new Entrenador(11, "Misty", 800);
        misty.agregarPokemonEquipo(PokemonCatalogo.crearSquirtle());
        misty.agregarPokemonEquipo(PokemonCatalogo.crearAbra());
        return misty;
    }

    private static Entrenador crearBrock() {
        Entrenador brock = new Entrenador(12, "Brock", 1200);
        brock.agregarPokemonEquipo(PokemonCatalogo.crearGeodude());
        brock.agregarPokemonEquipo(PokemonCatalogo.crearPikachu());
        brock.agregarPokemonEquipo(PokemonCatalogo.crearPidgey());
        return brock;
    }

    public static Entrenador crearRival(int indice) {
        return switch (indice) {
            case 0 -> crearGary();
            case 1 -> crearMisty();
            case 2 -> crearBrock();
            default -> throw new IllegalArgumentException("Rival desconocido: " + indice);
        };
    }
}
