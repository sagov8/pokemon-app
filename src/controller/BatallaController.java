package controller;

import model.Inventario;
import model.Objeto;
import model.pokemon.*;
import service.Batalla;
import view.ConsoleView;

import java.util.List;

public class BatallaController {

    private final ConsoleView vista;
    private Pokemon ultimoPokemonJugador;

    public BatallaController(ConsoleView vista) {
        this.vista = vista;
    }

    // ── Entrada pública ───────────────────────────────────────────────────

    /** Retorna true si el jugador ganó la batalla. */
    public boolean iniciarBatalla(Entrenador jugador, Entrenador rival) {
        Batalla batalla = new Batalla(jugador, rival);
        batalla.agregarObservador(vista);
        batalla.iniciar();

        ultimoPokemonJugador = jugador.pokemonActivo().orElse(null);

        while (!batalla.haTerminado()) {
            if (batalla.getTurnoActual() == jugador) {
                manejarTurnoJugador(batalla, jugador, rival);
            } else {
                manejarTurnoRival(batalla, rival);
            }
        }

        return batalla.getGanador()
                .map(g -> g == jugador)
                .orElse(false);
    }

    // ── Turno del jugador ─────────────────────────────────────────────────

    private void manejarTurnoJugador(Batalla batalla, Entrenador jugador, Entrenador rival) {
        notificarCambioAutomatico(jugador);
        vista.mostrarEstadoBatalla(jugador, rival);

        int opcion = vista.pedirOpcionMenu("¿Qué hacer?",
                "Atacar", "Usar objeto", "Cambiar pokemon", "Rendirse");

        switch (opcion) {
            case 1 -> manejarAtaque(batalla, jugador);
            case 2 -> manejarObjeto(batalla, jugador);
            case 3 -> manejarCambio(batalla, jugador);
            case 4 -> manejarRendicion(batalla);
        }
    }

    /** Detecta si el pokemon activo cambió (por debilitado) y avisa al jugador. */
    private void notificarCambioAutomatico(Entrenador jugador) {
        Pokemon actual = jugador.pokemonActivo().orElse(null);
        if (actual != null
                && ultimoPokemonJugador != null
                && !actual.equals(ultimoPokemonJugador)
                && ultimoPokemonJugador.estaDebilitado()) {
            vista.mostrarMensaje("\n¡" + actual.getNombre() + " entra en batalla!");
        }
        ultimoPokemonJugador = actual;
    }

    private void manejarAtaque(Batalla batalla, Entrenador jugador) {
        Pokemon pokemon = jugador.pokemonActivo().orElse(null);
        if (pokemon == null) return;

        List<Movimiento> movimientos = pokemon.getMovimientos();
        int eleccion = vista.pedirMovimiento(movimientos);
        batalla.ejecutarTurno(movimientos.get(eleccion - 1));

        ultimoPokemonJugador = jugador.pokemonActivo().orElse(ultimoPokemonJugador);
    }

    private void manejarObjeto(Batalla batalla, Entrenador jugador) {
        Inventario inv = jugador.getInventario();
        if (inv.estaVacio()) {
            vista.mostrarMensaje("No tienes objetos disponibles.");
            return; // no gasta turno
        }

        Objeto objeto = vista.pedirObjetoInventario(inv);
        if (objeto == null) return; // canceló, no gasta turno

        Pokemon objetivo = jugador.pokemonActivo().orElse(null);
        if (objetivo == null) return;

        boolean exito = jugador.usarObjeto(objeto.id(), objetivo);
        if (exito) {
            vista.mostrarMensaje(objeto.nombre() + " usado en " + objetivo.getNombre() + ".");
            batalla.pasarTurno(); // usar objeto gasta el turno
        } else {
            vista.mostrarMensaje("No se pudo usar " + objeto.nombre() + " ahora.");
        }
    }

    private void manejarCambio(Batalla batalla, Entrenador jugador) {
        Pokemon actual = jugador.pokemonActivo().orElse(null);
        Pokemon nuevo  = vista.pedirPokemonParaCambio(jugador.getEquipoActivo(), actual);

        if (nuevo == null) return; // canceló, no gasta turno

        batalla.cambiarPokemon(nuevo);
        ultimoPokemonJugador = nuevo;
        vista.mostrarMensaje("¡" + nuevo.getNombre() + " entra en batalla!");
    }

    private void manejarRendicion(Batalla batalla) {
        int confirmacion = vista.pedirOpcionMenu(
                "¿Seguro que quieres rendirte?", "Sí, me rindo", "No, continuar");
        if (confirmacion == 1) {
            batalla.rendirse();
        }
    }

    // ── Turno del rival (IA simple) ───────────────────────────────────────

    private void manejarTurnoRival(Batalla batalla, Entrenador rival) {
        Pokemon pokemon = rival.pokemonActivo().orElse(null);
        if (pokemon == null) return;

        List<Movimiento> movimientos = pokemon.getMovimientos();
        if (movimientos.isEmpty()) return;

        int idx = (int) (Math.random() * movimientos.size());
        batalla.ejecutarTurno(movimientos.get(idx));
    }
}
