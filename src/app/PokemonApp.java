package app;

import controller.BatallaController;
import factory.*;
import model.pokemon.*;
import view.ConsoleView;

public class PokemonApp {

    private final ConsoleView vista;
    private final BatallaController batallaController;

    public PokemonApp() {
        this.vista             = new ConsoleView();
        this.batallaController = new BatallaController(vista);
    }

    public void iniciar() {
        Entrenador jugador = crearJugador();
        Entrenador rival   = crearRival();
        batallaController.iniciarBatalla(jugador, rival);
    }

    private static Entrenador crearJugador() {
        Pokemon charmander = new Pokemon(1, "Charmander", TipoPokemon.FUEGO,
                5, 100, 30, 20, 25);
        charmander.agregarMovimiento(new CreadorMovimientoFuego().prepararMovimiento());
        charmander.agregarMovimiento(new CreadorMovimientoAire().prepararMovimiento());

        Entrenador jugador = new Entrenador(1, "Ash", 1000);
        jugador.agregarPokemonEquipo(charmander);
        return jugador;
    }

    private static Entrenador crearRival() {
        Pokemon squirtle = new Pokemon(2, "Squirtle", TipoPokemon.AGUA,
                5, 110, 25, 25, 20);
        squirtle.agregarMovimiento(new CreadorMovimientoAgua().prepararMovimiento());
        squirtle.agregarMovimiento(new CreadorMovimientoTierra().prepararMovimiento());

        Entrenador rival = new Entrenador(2, "Gary", 1000);
        rival.agregarPokemonEquipo(squirtle);
        return rival;
    }
}
