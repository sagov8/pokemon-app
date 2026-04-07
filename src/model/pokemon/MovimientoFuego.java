package model.pokemon;

public class MovimientoFuego implements Movimiento {
    private final String nombre;
    private final int poder;
    private final String tipo;

    public MovimientoFuego(String nombre, int poder) {
        this.nombre = nombre;
        this.poder = poder;
        this.tipo = "Fuego";
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getPoder() {
        return poder;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void usarMovimiento() {
        System.out.println("Se usa el movimiento de fuego: " + nombre);
    }

    @Override
    public int calcularDanio() {
        return poder + 10;
    }
}
