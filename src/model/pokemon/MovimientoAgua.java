package model.pokemon;

public class MovimientoAgua implements Movimiento {
    private final String nombre;
    private final int poder;
    private final String tipo;

    public MovimientoAgua(String nombre, int poder) {
        this.nombre = nombre;
        this.poder = poder;
        this.tipo = "Agua";
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
        System.out.println("Se usa el movimiento de agua: " + nombre);
    }

    @Override
    public int calcularDanio() {
        return poder + 5;
    }
}
