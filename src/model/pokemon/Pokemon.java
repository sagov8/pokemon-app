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
    private Pokemon evolucion;

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
    }

    // ── Combate ───────────────────────────────────────────────────────────

    /**
     * Ejecuta la animación/mensaje del movimiento.
     * Retorna el daño BASE del movimiento (sin stats ni efectividad).
     * Batalla se encarga de aplicar el daño final al defensor.
     */
    public int atacar(Movimiento movimiento) {
        if (estaDebilitado()) return 0;
        movimiento.usarMovimiento();          // imprime el mensaje del movimiento
        return movimiento.calcularDanio();    // daño base: poder + bonus del movimiento
    }

    /**
     * Recibe el daño final ya calculado por Batalla.
     */
    public void recibirDanio(int danio) {
        vidaActual = Math.max(0, vidaActual - Math.max(0, danio));
        if (vidaActual == 0) estado = EstadoPokemon.DEBILIDAD;
    }

    public void restaurarVida(int cantidad) {
        if (estaDebilitado()) return;
        vidaActual = Math.min(vidaMaxima, vidaActual + cantidad);
    }

    public void revivir() {
        if (!estaDebilitado()) return;
        estado = EstadoPokemon.ACTIVO;
        vidaActual = vidaMaxima / 2;
    }

    // ── Experiencia y nivel ───────────────────────────────────────────────

    public void ganarExperiencia(int exp) {
        if (estaDebilitado()) return;
        experiencia += exp;
        while (experiencia >= nivel * EXP_POR_NIVEL) {
            experiencia -= nivel * EXP_POR_NIVEL;
            subirNivel();
        }
    }

    private void subirNivel() {
        nivel++;
        vidaMaxima += nivel * 2;
        vidaActual += nivel * 2;
        ataque += nivel;
        defensa += nivel;
        velocidad += nivel / 2;
        System.out.println("¡" + nombre + " subió al nivel " + nivel + "!");
        verificarEvolucion();
    }

    private void verificarEvolucion() {
        if (evolucion != null && nivel >= 16) {
            System.out.println("¡" + nombre + " puede evolucionar!");
        }
    }

    public boolean evolucionar() {
        if (evolucion == null || nivel < 16) return false;
        System.out.println("¡" + nombre + " está evolucionando!");
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

    public double porcentajeVida() {
        return (double) vidaActual / vidaMaxima;
    }

    // ── Setter ────────────────────────────────────────────────────────────

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