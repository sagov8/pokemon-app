package model.pokemon;

public class MovimientoAire implements Movimiento {
    private final String nombre;
    private final int poder;
    private final String tipo;

    public MovimientoAire(String nombre, int poder) {
        this.nombre = nombre;
        this.poder = poder;
        this.tipo = "Aire";
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
        System.out.println("Se usa el movimiento de aire: " + nombre);
    }

    @Override
    public int calcularDanio() {
        return poder + 20;
    }
}
