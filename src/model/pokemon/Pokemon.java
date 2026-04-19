package model.pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pokemon {
    private static final int MAX_MOVIMIENTOS = 4;
    private static final int EXP_POR_NIVEL = 100;

    private final int id;
    private final String nombre;
    private final TipoPokemon tipo;

    private int nivel;
    private int experiencia;

    private int vidaActual;
    private int vidaMaxima;
    private int ataque;
    private int defensa;
    private int velocidad;

    private EstadoPokemon estado;

    private final List<Movimiento> movimientos = new ArrayList<>();
    private Pokemon evolucion; // null si no tiene evolución

    public Pokemon(int id, String nombre, TipoPokemon tipo,
                   int nivel, int vidaMaxima,
                   int ataque, int defensa, int velocidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivel = nivel;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.estado = EstadoPokemon.ACTIVO;
        this.experiencia = 0;
    }

    // ── Combate ──────────────────────────────────────────────────────────

    public void atacar(Pokemon defensor, Movimiento movimiento) {
        if (estaDebilitado()) return;
        movimiento.usarMovimiento(this, defensor);
    }

    public void recibirDanio(int danio) {
        if (danio < 0) danio = 0;
        vidaActual = Math.max(0, vidaActual - danio);
        if (vidaActual == 0) {
            estado = EstadoPokemon.DEBILIDAD;
        }
    }

    public void restaurarVida(int cantidad) {
        if (estaDebilitado()) return;
        vidaActual = Math.min(vidaMaxima, vidaActual + cantidad);
    }

    public void revivir() {
        if (!estaDebilitado()) return;
        estado = EstadoPokemon.ACTIVO;
        vidaActual = vidaMaxima / 2; // revive con la mitad de vida
    }

    // ── Experiencia y nivel ───────────────────────────────────────────────

    public void ganarExperiencia(int exp) {
        if (estaDebilitado()) return;
        experiencia += exp;
        while (experiencia >= expNecesariaParaSiguienteNivel()) {
            experiencia -= expNecesariaParaSiguienteNivel();
            subirNivel();
        }
    }

    private int expNecesariaParaSiguienteNivel() {
        return nivel * EXP_POR_NIVEL;
    }

    private void subirNivel() {
        nivel++;
        // Incremento de stats al subir nivel
        int bonus = nivel * 2;
        vidaMaxima += bonus;
        vidaActual += bonus;
        ataque += nivel;
        defensa += nivel;
        velocidad += nivel / 2;

        System.out.println("¡" + nombre + " subió al nivel " + nivel + "!");
        verificarEvolucion();
    }

    private void verificarEvolucion() {
        if (evolucion != null && nivel >= 16) {
            System.out.println("¡" + nombre + " puede evolucionar! (acepta/rechaza en el menú)");
        }
    }

    /**
     * Aplica la evolución: este pokemon adopta los stats del siguiente en la cadena.
     * Retorna true si la evolución fue exitosa.
     */
    public boolean evolucionar() {
        if (evolucion == null || nivel < 16) return false;
        System.out.println("¡" + nombre + " está evolucionando!");
        // La evolución la gestiona Entrenador/JuegoService para reemplazar la referencia
        return true;
    }

    // ── Movimientos ───────────────────────────────────────────────────────

    public boolean agregarMovimiento(Movimiento movimiento) {
        if (movimientos.size() >= MAX_MOVIMIENTOS) return false;
        movimientos.add(movimiento);
        return true;
    }

    public boolean reemplazarMovimiento(int indice, Movimiento nuevo) {
        if (indice < 0 || indice >= movimientos.size()) return false;
        movimientos.set(indice, nuevo);
        return true;
    }

    // ── Estado ────────────────────────────────────────────────────────────

    public boolean estaDebilitado() {
        return estado == EstadoPokemon.DEBILIDAD;
    }

    public boolean estaActivo() {
        return estado == EstadoPokemon.ACTIVO;
    }

    /**
     * Porcentaje de vida restante, útil para la barra de HP.
     */
    public double porcentajeVida() {
        return (double) vidaActual / vidaMaxima;
    }

    // ── Setters controlados ───────────────────────────────────────────────

    public void setEvolucion(Pokemon evolucion) {
        this.evolucion = evolucion;
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPokemon getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public EstadoPokemon getEstado() {
        return estado;
    }

    public Pokemon getEvolucion() {
        return evolucion;
    }

    public List<Movimiento> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    @Override
    public String toString() {
        return "%s [%s] Nv.%d — HP: %d/%d | ATK:%d DEF:%d VEL:%d".formatted(
                nombre, tipo, nivel, vidaActual, vidaMaxima, ataque, defensa, velocidad);
    }
}
