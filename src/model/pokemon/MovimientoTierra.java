package model.pokemon;

public class MovimientoTierra implements Movimiento {
    private final String nombre;
    private final int poder;
    private final String tipo;

    public MovimientoTierra(String nombre, int poder) {
        this.nombre = nombre;
        this.poder = poder;
        this.tipo = "Tierra";
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
        System.out.println("Se usa el movimiento de tierra: " + nombre);
    }

    @Override
    public int calcularDanio() {
        return poder + 4;
    }
}
