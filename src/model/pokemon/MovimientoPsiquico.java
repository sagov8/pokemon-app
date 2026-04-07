package model.pokemon;

public class MovimientoPsiquico implements Movimiento {

    private final String nombre;
    private final int poder;
    private final String tipo;

    public MovimientoPsiquico(String nombre, int poder) {
        this.nombre = nombre;
        this.poder = poder;
        this.tipo = "Psiquico";
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
        System.out.println("Se usa el movimiento psiquico: " + nombre);
    }

    @Override
    public int calcularDanio() {
        return poder + 10;
    }
}
