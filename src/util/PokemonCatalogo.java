package util;

import factory.*;
import model.pokemon.*;
import java.util.List;

public class PokemonCatalogo {

    private PokemonCatalogo() {}

    public static List<Pokemon> getStarters() {
        return List.of(crearCharmander(), crearSquirtle(), crearGeodude());
    }

    public static Pokemon crearPorId(int id) {
        return switch (id) {
            case 1  -> crearCharmander();
            case 2  -> crearSquirtle();
            case 3  -> crearGeodude();
            case 4  -> crearPikachu();
            case 5  -> crearAbra();
            case 6  -> crearPidgey();
            default -> throw new IllegalArgumentException("Pokemon desconocido: " + id);
        };
    }

    public static Pokemon crearEvolucionPorId(int id) {
        return switch (id) {
            case 11 -> crearCharmeleon();
            case 12 -> crearWartortle();
            case 13 -> crearGraveler();
            case 14 -> crearRaichu();
            case 15 -> crearKadabra();
            case 16 -> crearPidgeotto();
            default -> throw new IllegalArgumentException("Evolución desconocida: " + id);
        };
    }

    /**
     * Asigna los movimientos por defecto según el tipo del pokemon.
     * Usado por GameRepository al reconstruir pokemon desde archivo.
     */
    public static void asignarMovimientosPorTipo(Pokemon pokemon) {
        switch (pokemon.getTipo()) {
            case FUEGO     -> {
                pokemon.agregarMovimiento(new CreadorMovimientoFuego().prepararMovimiento());
                pokemon.agregarMovimiento(new CreadorMovimientoAire().prepararMovimiento());
            }
            case AGUA      -> {
                pokemon.agregarMovimiento(new CreadorMovimientoAgua().prepararMovimiento());
                pokemon.agregarMovimiento(new CreadorMovimientoTierra().prepararMovimiento());
            }
            case TIERRA    -> {
                pokemon.agregarMovimiento(new CreadorMovimientoTierra().prepararMovimiento());
                pokemon.agregarMovimiento(new CreadorMovimientoElectrico().prepararMovimiento());
            }
            case ELECTRICO -> {
                pokemon.agregarMovimiento(new CreadorMovimientoElectrico().prepararMovimiento());
                pokemon.agregarMovimiento(new CreadorMovimientoAire().prepararMovimiento());
            }
            case PSIQUICO  -> {
                pokemon.agregarMovimiento(new CreadorMovimientoPsiquico().prepararMovimiento());
                pokemon.agregarMovimiento(new CreadorMovimientoElectrico().prepararMovimiento());
            }
            case AIRE      -> {
                pokemon.agregarMovimiento(new CreadorMovimientoAire().prepararMovimiento());
                pokemon.agregarMovimiento(new CreadorMovimientoFuego().prepararMovimiento());
            }
        }
    }

    // ── Pokemon base ──────────────────────────────────────────────────────

    public static Pokemon crearCharmander() {
        Pokemon p = new Pokemon(1, "Charmander", TipoPokemon.FUEGO, 5, 100, 52, 43, 65);
        asignarMovimientosPorTipo(p);
        p.setEvolucion(crearCharmeleon());
        return p;
    }

    public static Pokemon crearSquirtle() {
        Pokemon p = new Pokemon(2, "Squirtle", TipoPokemon.AGUA, 5, 110, 48, 65, 43);
        asignarMovimientosPorTipo(p);
        p.setEvolucion(crearWartortle());
        return p;
    }

    public static Pokemon crearGeodude() {
        Pokemon p = new Pokemon(3, "Geodude", TipoPokemon.TIERRA, 5, 120, 80, 100, 20);
        asignarMovimientosPorTipo(p);
        p.setEvolucion(crearGraveler());
        return p;
    }

    public static Pokemon crearPikachu() {
        Pokemon p = new Pokemon(4, "Pikachu", TipoPokemon.ELECTRICO, 5, 90, 55, 40, 90);
        asignarMovimientosPorTipo(p);
        p.setEvolucion(crearRaichu());
        return p;
    }

    public static Pokemon crearAbra() {
        Pokemon p = new Pokemon(5, "Abra", TipoPokemon.PSIQUICO, 5, 85, 75, 35, 90);
        asignarMovimientosPorTipo(p);
        p.setEvolucion(crearKadabra());
        return p;
    }

    public static Pokemon crearPidgey() {
        Pokemon p = new Pokemon(6, "Pidgey", TipoPokemon.AIRE, 5, 95, 45, 40, 56);
        asignarMovimientosPorTipo(p);
        p.setEvolucion(crearPidgeotto());
        return p;
    }

    // ── Evoluciones ───────────────────────────────────────────────────────

    public static Pokemon crearCharmeleon() {
        Pokemon p = new Pokemon(11, "Charmeleon", TipoPokemon.FUEGO, 16, 155, 64, 58, 80);
        asignarMovimientosPorTipo(p);
        return p;
    }

    public static Pokemon crearWartortle() {
        Pokemon p = new Pokemon(12, "Wartortle", TipoPokemon.AGUA, 16, 165, 58, 80, 58);
        asignarMovimientosPorTipo(p);
        return p;
    }

    public static Pokemon crearGraveler() {
        Pokemon p = new Pokemon(13, "Graveler", TipoPokemon.TIERRA, 16, 175, 95, 115, 35);
        asignarMovimientosPorTipo(p);
        return p;
    }

    public static Pokemon crearRaichu() {
        Pokemon p = new Pokemon(14, "Raichu", TipoPokemon.ELECTRICO, 16, 150, 90, 55, 110);
        asignarMovimientosPorTipo(p);
        return p;
    }

    public static Pokemon crearKadabra() {
        Pokemon p = new Pokemon(15, "Kadabra", TipoPokemon.PSIQUICO, 16, 130, 105, 45, 105);
        asignarMovimientosPorTipo(p);
        return p;
    }

    public static Pokemon crearPidgeotto() {
        Pokemon p = new Pokemon(16, "Pidgeotto", TipoPokemon.AIRE, 16, 145, 60, 55, 71);
        asignarMovimientosPorTipo(p);
        return p;
    }
}