package model.pokemon;

import model.Inventario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Entrenador {

    private static final int MAX_EQUIPO = 6;

    private final int id;
    private final String nombre;
    private double dinero;

    private final List<Pokemon> equipoActivo = new ArrayList<>();
    private final List<Pokemon> almacenamiento = new ArrayList<>();
    private final Inventario inventario = new Inventario();

    public Entrenador(int id, String nombre, double dinero) {
        this.id = id;
        this.nombre = nombre;
        this.dinero = dinero;
    }

    // Equipo

    public boolean agregarPokemonEquipo(Pokemon pokemon) {
        if (equipoActivo.size() >= MAX_EQUIPO) return false;
        equipoActivo.add(pokemon);
        return true;
    }

    public void almacenarPokemon(Pokemon pokemon) {
        if (equipoActivo.remove(pokemon)) {
            almacenamiento.add(pokemon);
        }
    }

    public boolean recuperarPokemon(Pokemon pokemon) {
        if (!almacenamiento.contains(pokemon)) return false;
        if (equipoActivo.size() >= MAX_EQUIPO) return false;
        almacenamiento.remove(pokemon);
        equipoActivo.add(pokemon);
        return true;
    }

    /**
     * Retorna el primer pokemon vivo del equipo.
     * Retorna Optional vacío si todos están debilitados.
     */
    public Optional<Pokemon> pokemonActivo() {
        return equipoActivo.stream()
                .filter(Pokemon::estaActivo)
                .findFirst();
    }

    public boolean tieneEquipoVivo() {
        return equipoActivo.stream().anyMatch(Pokemon::estaActivo);
    }

    public int nivelPromedio() {
        return (int) equipoActivo.stream()
                .mapToInt(Pokemon::getNivel)
                .average()
                .orElse(1);
    }

    // Economía

    public void ganarDinero(double cantidad) {
        if (cantidad > 0) dinero += cantidad;
    }

    public boolean gastarDinero(double cantidad) {
        if (cantidad > dinero) return false;
        dinero -= cantidad;
        return true;
    }

    // Inventario

    public boolean usarObjeto(int idObjeto, Pokemon objetivo) {
        return inventario.usarObjeto(idObjeto, objetivo);
    }

    public boolean evolucionarPokemon(Pokemon pokemon) {
        int idx = equipoActivo.indexOf(pokemon);
        if (idx == -1) return false;
        if (!pokemon.evolucionar()) return false;
        equipoActivo.set(idx, pokemon.getEvolucion());
        return true;
    }

    /** Solo para uso de GameRepository al cargar una partida. */
    public void agregarAlAlmacenamiento(Pokemon pokemon) {
        almacenamiento.add(pokemon);
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getDinero() {
        return dinero;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public List<Pokemon> getEquipoActivo() {
        return Collections.unmodifiableList(equipoActivo);
    }

    public List<Pokemon> getAlmacenamiento() {
        return Collections.unmodifiableList(almacenamiento);
    }

    @Override
    public String toString() {
        return "Entrenador %s (Nv. promedio: %d | $%.0f)".formatted(nombre, nivelPromedio(), dinero);
    }
}
