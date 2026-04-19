package controller;

import model.pokemon.Entrenador;
import model.pokemon.Movimiento;
import model.pokemon.Pokemon;
import service.Batalla;
import view.ConsoleView;

import java.util.List;

public class BatallaController {

    private final ConsoleView vista;

    public BatallaController(ConsoleView vista) {
        this.vista = vista;
    }

    public void iniciarBatalla(Entrenador jugador, Entrenador rival) {
        Batalla batalla = new Batalla(jugador, rival);
        batalla.agregarObservador(vista);
        batalla.iniciar();

        while (!batalla.haTerminado()) {
            Entrenador turno = batalla.getTurnoActual();
            Pokemon pokemon  = turno.pokemonActivo().orElse(null);

            if (pokemon == null) break;

            vista.mostrarEstadoBatalla(jugador, rival);

            List<Movimiento> movimientos = pokemon.getMovimientos();
            int eleccion = vista.pedirMovimiento(movimientos);
            batalla.ejecutarTurno(movimientos.get(eleccion - 1));
        }
    }
}
