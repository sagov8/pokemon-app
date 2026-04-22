package service;

import model.pokemon.Entrenador;
import model.pokemon.EstadoBatalla;
import model.pokemon.Movimiento;
import model.pokemon.Pokemon;
import model.pokemon.TipoPokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Batalla {

    private static final double RECOMPENSA_DINERO = 500.0;
    private static final int EXP_POR_DANIO = 5;

    private final Entrenador entrenador1;
    private final Entrenador entrenador2;
    // Nuevo campo (junto a los otros atributos)
    private Entrenador ganador;

    private Entrenador turnoActual;
    private EstadoBatalla estado;

    private final List<BatallaObserver> observadores = new ArrayList<>();

    // ── Constructor ───────────────────────────────────────────────────────

    public Batalla(Entrenador entrenador1, Entrenador entrenador2) {
        this.entrenador1 = entrenador1;
        this.entrenador2 = entrenador2;
        this.estado = EstadoBatalla.EN_CURSO;
        this.turnoActual = determinarPrimero();
    }

    /**
     * El pokemon más rápido mueve primero.
     * En caso de empate mueve el entrenador1.
     */
    private Entrenador determinarPrimero() {
        int vel1 = entrenador1.pokemonActivo()
                .map(Pokemon::getVelocidad).orElse(0);
        int vel2 = entrenador2.pokemonActivo()
                .map(Pokemon::getVelocidad).orElse(0);
        return vel1 >= vel2 ? entrenador1 : entrenador2;
    }

    // ── Observer: registro ────────────────────────────────────────────────

    public void agregarObservador(BatallaObserver observador) {
        observadores.add(observador);
    }

    public void removerObservador(BatallaObserver observador) {
        observadores.remove(observador);
    }

    // ── Observer: notificaciones ──────────────────────────────────────────

    private void notificarInicio() {
        Entrenador segundo = rival();
        observadores.forEach(o -> o.onBatallaIniciada(turnoActual, segundo));
    }

    private void notificarTurno() {
        Entrenador actual = turnoActual;
        observadores.forEach(o -> o.onTurnoIniciado(actual));
    }

    private void notificarMovimiento(Pokemon atacante, Pokemon defensor,
                                     Movimiento movimiento, int danio, double efectividad) {
        observadores.forEach(o ->
                o.onMovimientoUsado(atacante, defensor, movimiento, danio, efectividad));
    }

    private void notificarDebilitado(Pokemon pokemon, Entrenador entrenador) {
        observadores.forEach(o -> o.onPokemonDebilitado(pokemon, entrenador));
    }

    private void notificarFin(Entrenador ganador, Entrenador perdedor) {
        observadores.forEach(o -> o.onBatallaFinalizada(ganador, perdedor));
    }

    // ── API pública ───────────────────────────────────────────────────────

    /**
     * Debe llamarse una vez después de registrar todos los observadores.
     */
    public void iniciar() {
        validarEquipos();
        notificarInicio();
        notificarTurno();
    }

    /**
     * Ejecuta el movimiento elegido por el entrenador en turno.
     * Lanza IllegalStateException si la batalla ya terminó.
     * Lanza IllegalArgumentException si el movimiento no pertenece al pokemon activo.
     */
    public void ejecutarTurno(Movimiento movimiento) {
        if (estado == EstadoBatalla.FINALIZADO) {
            throw new IllegalStateException("La batalla ya finalizó.");
        }

        Entrenador rivalActual = rival();

        Pokemon atacante = turnoActual.pokemonActivo()
                .orElseThrow(() -> new IllegalStateException(
                        turnoActual.getNombre() + " no tiene pokemon activo."));

        Pokemon defensor = rivalActual.pokemonActivo()
                .orElseThrow(() -> new IllegalStateException(
                        rivalActual.getNombre() + " no tiene pokemon activo."));

        validarMovimiento(atacante, movimiento);

        // ── Cálculo de daño ───────────────────────────────────────────────
        int danioBase = atacante.atacar(movimiento);
        double modificador = (double) atacante.getAtaque() / Math.max(1, defensor.getDefensa());
        double efectividad = resolverEfectividad(movimiento, defensor);
        int danioFinal = (int) Math.max(1, danioBase * modificador * efectividad);

        // ── Aplicar daño ──────────────────────────────────────────────────
        defensor.recibirDanio(danioFinal);
        notificarMovimiento(atacante, defensor, movimiento, danioFinal, efectividad);

        // ── Verificar debilitado ──────────────────────────────────────────
        if (defensor.estaDebilitado()) {
            notificarDebilitado(defensor, rivalActual);
            atacante.ganarExperiencia(danioFinal * EXP_POR_DANIO);
        }

        // ── Verificar fin de batalla ──────────────────────────────────────
        if (!rivalActual.tieneEquipoVivo()) {
            finalizarBatalla(turnoActual, rivalActual);
            return;
        }

        cambiarTurno();
    }

    /**
     * Permite al entrenador en turno cambiar su pokemon activo sin gastar turno
     * solo si el pokemon actual está debilitado; si está vivo, gasta el turno.
     */
    public void cambiarPokemon(Pokemon nuevo) {
        if (estado == EstadoBatalla.FINALIZADO) {
            throw new IllegalStateException("La batalla ya finalizó.");
        }

        Optional<Pokemon> actual = turnoActual.pokemonActivo();
        boolean activoDebilitado = actual.isEmpty() || actual.get().estaDebilitado();

        if (!turnoActual.getEquipoActivo().contains(nuevo) || nuevo.estaDebilitado()) {
            throw new IllegalArgumentException("Pokemon inválido para el cambio.");
        }

        // Si el activo no está debilitado, cambiar gasta el turno
        if (!activoDebilitado) {
            cambiarTurno();
        }
    }

    // ── Helpers privados ──────────────────────────────────────────────────

    private Entrenador rival() {
        return turnoActual == entrenador1 ? entrenador2 : entrenador1;
    }

    private void cambiarTurno() {
        turnoActual = rival();
        notificarTurno();
    }

    // Actualizar finalizarBatalla para registrarlo
    private void finalizarBatalla(Entrenador ganador, Entrenador perdedor) {
        this.ganador = ganador;
        estado = EstadoBatalla.FINALIZADO;
        ganador.ganarDinero(RECOMPENSA_DINERO);
        notificarFin(ganador, perdedor);
    }

    private double resolverEfectividad(Movimiento movimiento, Pokemon defensor) {
        try {
            TipoPokemon tipoMovimiento = TipoPokemon.desdeCadena(movimiento.getTipo());
            return tipoMovimiento.efectividadContra(defensor.getTipo());
        } catch (IllegalArgumentException e) {
            // Si el tipo del movimiento no está en el enum, no hay modificador
            return 1.0;
        }
    }

    private void validarMovimiento(Pokemon pokemon, Movimiento movimiento) {
        boolean pertenece = pokemon.getMovimientos().contains(movimiento);
        if (!pertenece) {
            throw new IllegalArgumentException(
                    pokemon.getNombre() + " no conoce el movimiento: " + movimiento.getNombre());
        }
    }

    private void validarEquipos() {
        if (entrenador1.pokemonActivo().isEmpty()) {
            throw new IllegalStateException(entrenador1.getNombre() + " no tiene pokemon activos.");
        }
        if (entrenador2.pokemonActivo().isEmpty()) {
            throw new IllegalStateException(entrenador2.getNombre() + " no tiene pokemon activos.");
        }
    }

    // Métodos nuevos
    public void pasarTurno() {
        if (estado == EstadoBatalla.FINALIZADO) return;
        cambiarTurno();
    }

    public void rendirse() {
        if (estado == EstadoBatalla.FINALIZADO) return;
        finalizarBatalla(rival(), turnoActual);
    }

    public Optional<Entrenador> getGanador() {
        return Optional.ofNullable(ganador);
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public Entrenador getTurnoActual() {
        return turnoActual;
    }

    public EstadoBatalla getEstado() {
        return estado;
    }

    public Entrenador getEntrenador1() {
        return entrenador1;
    }

    public Entrenador getEntrenador2() {
        return entrenador2;
    }

    public boolean haTerminado() {
        return estado == EstadoBatalla.FINALIZADO;
    }
}
