package service;

import model.pokemon.Entrenador;
import model.pokemon.Movimiento;
import model.pokemon.Pokemon;

public interface BatallaObserver {

    /** Se dispara al inicio de la batalla con el entrenador que mueve primero. */
    void onBatallaIniciada(Entrenador primero, Entrenador segundo);

    /** Se dispara al comienzo de cada turno. */
    void onTurnoIniciado(Entrenador turnoActual);

    /**
     * Se dispara después de ejecutar un movimiento.
     * Incluye el daño final ya aplicado y la efectividad para mostrar mensajes
     */
    void onMovimientoUsado(Pokemon atacante, Pokemon defensor,
                           Movimiento movimiento, int danioFinal, double efectividad);

    /** Se dispara cuando un pokemon llega a 0 HP. */
    void onPokemonDebilitado(Pokemon pokemon, Entrenador entrenador);

    /** Se dispara una sola vez cuando la batalla termina. */
    void onBatallaFinalizada(Entrenador ganador, Entrenador perdedor);
}